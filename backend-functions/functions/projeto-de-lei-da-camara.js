const axios = require('axios')
const { get } = require('lodash')

/**
* Obter projeto de lei da câmara
* @param {string} proposicaoId Id da proposição do senado
* @returns {object} Projeto de lei do senado
*/
module.exports = async (proposicaoId, context) => {
  const CASA = {
    id: 1,
    nome: 'Câmara dos Deputados'
  }

  const httpGetSenado = async (url) => {
    const res = await axios.get(url)
    return get(res, 'data.DetalheMateria.Materia', {})
  }

  const httpGetCamara = async (id) => {
    const url = `https://dadosabertos.camara.leg.br/api/v2/proposicoes/${id}`
    const res = await axios.get(url)
    return get(res, 'data.dados', {})
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

  const proposicaoDaCamara = await httpGetCamara(proposicaoId)
  const urlSenado = proposicaoDaCamara.uriPropAnterior || proposicaoDaCamara.uriPropPosterior
  const proposicaoDoSenado = await httpGetSenado(urlSenado)

  const regimeString = get(proposicaoDaCamara, 'statusProposicao.regime', '')
  const regimeMatch = regimeString.match(/(.+) \(.*\)/)
  const regime = (regimeMatch && regimeMatch[1]) || 'Desconhecido'
  const normaGerada = get(proposicaoDoSenado, 'NormaGerada.IdentificacaoNorma', {})

  return {
    id: get(proposicaoDoSenado, 'IdentificacaoMateria.CodigoMateria'),
    origem: get(proposicaoDoSenado, 'OrigemMateria.NomeCasaOrigem'),
    assunto: get(proposicaoDoSenado, 'Assunto.AssuntoGeral.Descricao'),
    siglaTipo: get(proposicaoDoSenado, 'IdentificacaoMateria.SiglaSubtipoMateria'),
    descricaoTipo: get(proposicaoDoSenado, 'IdentificacaoMateria.DescricaoSubtipoMateria'),
    numero: get(proposicaoDoSenado, 'IdentificacaoMateria.NumeroMateria'),
    ano: get(proposicaoDoSenado, 'IdentificacaoMateria.AnoMateria'),
    dataApresentacao: new Date(get(proposicaoDoSenado, 'DadosBasicosMateria.DataApresentacao')),
    regime,
    situacao: get(proposicaoDoSenado, 'SituacaoAtual.Autuacoes.Autuacao.Situacao.DescricaoSituacao'),
    tramitando: get(proposicaoDoSenado, 'IdentificacaoMateria.IndicadorTramitando') === 'Sim',
    ementa: get(proposicaoDoSenado, 'DadosBasicosMateria.EmentaMateria'),
    explicacaoEmenta: get(proposicaoDoSenado, 'DadosBasicosMateria.ExplicacaoEmentaMateria'),
    transformadoEm: {
      id: normaGerada.CodigoNorma,
      tipo: normaGerada.DescricaoTipoNorma,
      numero: normaGerada.NumeroNorma,
      ano: normaGerada.AnoNorma,
      data: new Date(normaGerada.DataNorma)
    },
    linhaDoTempo: obterLinhaDoTempo(proposicaoDoSenado),
    autores: formatarAutores(get(proposicaoDoSenado, 'Autoria.Autor')),
    proposicoesRelacionadas: obterProposicoesRelacionadas(proposicaoDoSenado),
    url: `https://www25.senado.leg.br/web/atividade/materias/-/materia/${proposicaoId}`,
    casa: CASA
  }
}
