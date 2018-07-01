const axios = require('axios')
const querystring = require('querystring')
const { get, random } = require('lodash')

/**
* Listar projetos de lei da CÂmara dos Deputados
* @returns {array} Lista de projetos de lei da câmara dos deputados
*/
module.exports = async (context) => {
  const CASA = {
    id: 1,
    nome: 'Câmara dos Deputados'
  }

  const urlProposicoes = query => `https://dadosabertos.camara.leg.br/api/v2/proposicoes?${query}`
  const urlAutores = id => `https://dadosabertos.camara.leg.br/api/v2/proposicoes/${id}/autores`

  const httpGet = async (url, def) => {
    if (!url) return {}
    const res = await axios.get(url)
    const { data = {} } = res
    return data.dados || def
  }

  const getRegime = (proposicaoDaCamara) => {
    const regimeString = get(proposicaoDaCamara, 'statusProposicao.regime', '')
    const regimeMatch = regimeString.match(/(.+) \(.*\)/)
    return (regimeMatch && regimeMatch[1]) || 'Desconhecido'
  }

  // Proposições

  const parametros = { ...context.params }
  parametros.siglaTipo = get(context, 'params.siglaTipo', 'PL')
  parametros.ordem = get(context, 'params.ordem', 'DESC')
  parametros.ordenarPor = get(context, 'params.ordenarPor', 'numero')
  const query = querystring.stringify(parametros)

  const proposicoes = await httpGet(urlProposicoes(query), [])
  const proposicoesUri = proposicoes.map(p => p.uri)
  const proposicoesDetalhesPromises = proposicoesUri.map(p => httpGet(p, {}))
  const proposicoesDetalhes = await Promise.all(proposicoesDetalhesPromises)

  // Autores

  const autores = await Promise.all(proposicoes.map(p => httpGet(urlAutores(p.id))))
  const autoresAleatorios = autores.map(a => a[random(0, a.length - 1)] || {})

  const autoresDetalhes = await Promise.all(autoresAleatorios.map(autor => {
    return autor.uri ? httpGet(autor.uri) : {}
  }))

  const autoresComFotos = autoresAleatorios.map((autor, index) => {
    const id = autor.uri && autor.uri.match(/deputados\/(.*)/)[1]
    const detalhes = autoresDetalhes[index].ultimoStatus || {}
    return {
      id,
      nome: autor.nome,
      tipo: autor.tipo,
      partido: detalhes.siglaPartido,
      foto: detalhes.urlFoto
    }
  })

  // Join

  return proposicoes.map((proposicao, index) => ({
    id: proposicao.id,
    dataApresentacao: new Date(get(proposicoesDetalhes[index], 'dataApresentacao')),
    siglaTipo: proposicao.siglaTipo,
    numero: proposicao.numero,
    regime: getRegime(proposicoesDetalhes[index]),
    assunto: 'Desconhecido', // TODO:
    origem: 'Desconhecido',
    situacao: get(proposicoesDetalhes[index], 'statusProposicao.descricaoSituacao'),
    ano: proposicao.ano,
    ementa: proposicao.ementa,
    autor: autoresComFotos[index],
    casa: CASA
  }))
}
