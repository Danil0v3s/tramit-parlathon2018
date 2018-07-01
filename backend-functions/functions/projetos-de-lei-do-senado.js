const axios = require('axios')
const querystring = require('querystring')
const { get, random, take, partial } = require('lodash')

/**
 * Listar projetos de lei da Senado federal
 * @returns {array} Lista de projetos de lei do senado
 */
module.exports = async (context) => {
  const CASA = {
    id: 2,
    nome: 'Senado Federal'
  }

  const urlProposicoes = (query) =>
    `http://legis.senado.leg.br/dadosabertos/materia/atualizadas?${query}`
  const urlProposicoesDetalhes = (id) =>
    `http://legis.senado.leg.br/dadosabertos/materia/${id}`

  const httpGetManyMaterias = async (url) => {
    const res = await axios.get(url)
    return get(res, 'data.ListaMateriasAtualizadas.Materias.Materia', [])
  }

  const httpGetOneMateria = async (url) => {
    const res = await axios.get(url)
    return get(res, 'data.DetalheMateria.Materia', {})
  }

  const httpGetCamara = async (camaraObj = {}) => {
    const { IdentificacaoMateria } = camaraObj

    if (!camaraObj || !IdentificacaoMateria) return {}

    const { SiglaSubtipoMateria, NumeroMateria, AnoMateria } = IdentificacaoMateria
    const query = querystring.stringify({
      siglaTipo: SiglaSubtipoMateria,
      ano: AnoMateria,
      numero: NumeroMateria
    })

    const url = `https://dadosabertos.camara.leg.br/api/v2/proposicoes?${query}`
    const res = await axios.get(url)

    const proposicaoUrl = get(res, 'data.dados[0].uri')
    const res2 = await axios.get(proposicaoUrl)
    return get(res2, 'data.dados', {})
  }

  const getAutorAleatorio = (proposicao) => {
    const autores = get(proposicao, 'Autoria.Autor', [])
    const autorAleatorio = autores[random(0, autores.length - 1)] || {}
    const getProp = partial(get, autorAleatorio)
    return {
      id: getProp('IdentificacaoParlamentar.CodigoParlamentar'),
      nome: getProp('NomeAutor'),
      foto: getProp('IdentificacaoParlamentar.UrlFotoParlamentar')
    }
  }
  
  const getRegime = async (proposicaoDoSenado) => {
    const outrosNumeros = get(proposicaoDoSenado, 'OutrosNumerosDaMateria.OutroNumeroDaMateria', [])
    const camaraObj = outrosNumeros.find(n => n.IdentificacaoMateria.SiglaCasaIdentificacaoMateria === 'CD') || {}
    const proposicaoDaCamara = await httpGetCamara(camaraObj)

    const regimeString = get(proposicaoDaCamara, 'statusProposicao.regime', '')
    const regimeMatch = regimeString.match(/(.+) \(.*\)/)
    return (regimeMatch && regimeMatch[1]) || 'Desconhecido'
  }

  const parametros = { ...context.params }
  parametros.sigla = get(context, 'params.siglaTipo', 'PLS')
  const query = querystring.stringify(parametros)
  const itens = get(context, 'params.itens', 15)

  const proposicoes = take(
    await httpGetManyMaterias(urlProposicoes(query)),
    itens
  )
  const proposicoesDetalhadas = await Promise.all(
    proposicoes
      .map(p => p.IdentificacaoMateria.CodigoMateria)
      .map(urlProposicoesDetalhes)
      .map(httpGetOneMateria)
  )

  const regimes = await Promise.all(proposicoesDetalhadas.map(getRegime))

  return proposicoesDetalhadas.map((proposicao, index) => ({
    id: proposicao.IdentificacaoMateria.CodigoMateria,
    siglaTipo: proposicao.IdentificacaoMateria.SiglaSubtipoMateria,
    dataApresentacao: new Date(proposicao.DadosBasicosMateria.DataApresentacao),
    situacao: proposicao.SituacaoAtual.Autuacoes.Autuacao.Situacao.DescricaoSituacao,
    regime: regimes[index],
    origem: proposicao.OrigemMateria.NomeCasaOrigem,
    assunto: proposicao.Assunto.AssuntoGeral.Descricao,
    numero: proposicao.IdentificacaoMateria.NumeroMateria,
    ano: proposicao.IdentificacaoMateria.AnoMateria,
    ementa: proposicao.DadosBasicosMateria.EmentaMateria,
    autor: getAutorAleatorio(proposicao),
    casa: CASA
  }))
}
