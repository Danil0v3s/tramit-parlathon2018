const querystring = require('querystring')
const axios = require('axios')
const { get } = require('lodash')

/**
* Obter projeto de lei do senado
* @param {string} proposicaoId Id da proposição do senado
* @returns {object} Projeto de lei do senado
*/
module.exports = async (proposicaoId, context) => {
  const CASA = {
    id: 2,
    nome: 'Senado Federal'
  }

  const httpGetSenado = async (id) => {
    const url = `http://legis.senado.leg.br/dadosabertos/materia/${id}`
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

  // TODO:
  const formatarAutores = (autores) => {
    return autores.map(autor => ({
      id: get(autor, 'IdentificacaoParlamentar.CodigoParlamentar'),
      nome: get(autor, 'NomeAutor'),
      foto: get(autor, 'IdentificacaoParlamentar.UrlFotoParlamentar', null)
    }))
  }

  // TODO:
  const obterLinhaDoTempo = (proposicao) => {
    return {
      casaIniciadora: new Date(),
      casaRevisadora: null,
      sancaoPresidencial: null,
      promulgacaoEPublicacao: null
    }
  }

  // TODO:
  const obterProposicoesRelacionadas = () => {
    return []
  }

  const proposicaoDoSenado = await httpGetSenado(proposicaoId)
  const outrosNumeros = get(proposicaoDoSenado, 'OutrosNumerosDaMateria.OutroNumeroDaMateria', [])
  const camaraObj = outrosNumeros.find(n => n.IdentificacaoMateria.SiglaCasaIdentificacaoMateria === 'CD') || {}
  const proposicaoDaCamara = await httpGetCamara(camaraObj)

  const regimeString = get(proposicaoDaCamara, 'statusProposicao.regime', '')
  const regimeMatch = regimeString.match(/(.+) \(.*\)/)
  const regime = (regimeMatch && regimeMatch[1]) || 'Desconhecido'
  const normaGerada = get(proposicaoDoSenado, 'NormaGerada.IdentificacaoNorma', {})

  return {
    id: proposicaoDoSenado.IdentificacaoMateria.CodigoMateria,
    origem: proposicaoDoSenado.OrigemMateria.NomeCasaOrigem,
    assunto: proposicaoDoSenado.Assunto.AssuntoGeral.Descricao,
    siglaTipo: proposicaoDoSenado.IdentificacaoMateria.SiglaSubtipoMateria,
    descricaoTipo: proposicaoDoSenado.IdentificacaoMateria.DescricaoSubtipoMateria,
    numero: proposicaoDoSenado.IdentificacaoMateria.NumeroMateria,
    ano: proposicaoDoSenado.IdentificacaoMateria.AnoMateria,
    dataApresentacao: new Date(proposicaoDoSenado.DadosBasicosMateria.DataApresentacao),
    regime,
    situacao: proposicaoDoSenado.SituacaoAtual.Autuacoes.Autuacao.Situacao.DescricaoSituacao,
    tramitando: proposicaoDoSenado.IdentificacaoMateria.IndicadorTramitando === 'Sim',
    ementa: proposicaoDoSenado.DadosBasicosMateria.EmentaMateria,
    explicacaoEmenta: proposicaoDoSenado.DadosBasicosMateria.ExplicacaoEmentaMateria,
    transformadoEm: {
      id: normaGerada.CodigoNorma,
      tipo: normaGerada.DescricaoTipoNorma,
      numero: normaGerada.NumeroNorma,
      ano: normaGerada.AnoNorma,
      data: new Date(normaGerada.DataNorma)
    },
    linhaDoTempo: obterLinhaDoTempo(proposicaoDoSenado),
    autores: formatarAutores(proposicaoDoSenado.Autoria.Autor),
    proposicoesRelacionadas: obterProposicoesRelacionadas(proposicaoDoSenado),
    url: `https://www25.senado.leg.br/web/atividade/materias/-/materia/${proposicaoId}`,
    casa: CASA
  }
}
