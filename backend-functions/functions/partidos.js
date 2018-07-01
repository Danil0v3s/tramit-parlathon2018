const axios = require('axios')

/**
* Listar partidos com fotos
* @returns {array} Lista de partidos com fotos
*/
module.exports = async (context) => {
  const httpGet = async (url) => {
    const res = await axios.get(url)
    const { data = {} } = res
    return data.dados || []
  }

  const {
    ordem = 'ASC',
    ordenarPor = 'sigla',
    pagina = 1,
    itens = 15
  } = context.params

  const urlPartidos = `https://dadosabertos.camara.leg.br/api/v2/partidos?ordem=${ordem}&ordenarPor=${ordenarPor}&pagina=${pagina}&itens=${itens}`
  const partidos = await httpGet(urlPartidos)
  const detalhesDosPartidos = await Promise.all(partidos.map(p => httpGet(p.uri)))

  return partidos.map(partido => ({
    id: partido.id,
    sigla: partido.sigla,
    nome: partido.nome,
    foto: detalhesDosPartidos.find(dp => dp.id === partido.id).urlLogo
  }))
}
