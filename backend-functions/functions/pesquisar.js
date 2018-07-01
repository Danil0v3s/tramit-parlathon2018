const querystring = require('querystring')
const axios = require('axios')
const {get, random, take, partial} = require('lodash')

module.exports = async (context) => {

    const {params} = context

    const urlProposicoesDetalhesSenado = id => `http://legis.senado.leg.br/dadosabertos/materia/${id}`
    const urlProposicoes = query => `https://dadosabertos.camara.leg.br/api/v2/proposicoes?${query}`
    const urlAutores = id => `https://dadosabertos.camara.leg.br/api/v2/proposicoes/${id}/autores`


    const httpGetCamara = async (params) => {
        const res = await axios.get("https://dadosabertos.camara.leg.br/api/v2/proposicoes", {params});
        return get(res, 'data.dados', [])
    }

    const httpGet = async (url, def) => {
        if (!url) return {}
        const res = await axios.get(url)
        const { data = {} } = res
        return data.dados || def
    }

    const httpGetSenado = async (params) => {
        const res = await axios.get("http://legis.senado.leg.br/dadosabertos/materia/pesquisa/lista", {
            params,
            headers: {'Accept': "application/json"}
        });
        let materias = get(res, 'data.PesquisaBasicaMateria.Materias.Materia', [])
        let list = [];
        if (materias instanceof Array) {
            list = [...materias]
        } else {
            list = [materias]
        }
        return list
    }

    const httpGetOneMateriaSenado = async (url) => {
        const res = await axios.get(url)
        return get(res, 'data.DetalheMateria.Materia', {})
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

    const getHousesParams = (type, params) => {
        /**
         * 1 = Camara
         * 2 = Senado
         */
        let p = {}
        if (type == 1) {
            if (params.numero) p.numero = params.numero
            if (params.ano) p.ano = params.ano
            if (params.tipo) {
                let tipo = params.tipo == "PLC" ? "PL" : params.tipo
                p.siglaTipo = tipo
            }
            if (params.keywords) p.keywords = params.keywords
            p.ordenarPor = "ano"
            p.ordem = "DESC"
        } else if (type == 2) {
            if (params.numero) p.numero = params.numero
            if (params.ano) p.ano = params.ano
            if (params.tipo) p.sigla = params.tipo
            if (params.keywords) p.palavraChave = params.keywords
        }

        return p
    }

    const proposicoesSenado = await httpGetSenado(getHousesParams(2, params));
    const proposicoesDetalhadasSenado = await Promise.all(
        proposicoesSenado
            .map(p => p.IdentificacaoMateria.CodigoMateria)
            .map(urlProposicoesDetalhesSenado)
            .map(httpGetOneMateriaSenado)
    )

    let propsSenado = proposicoesDetalhadasSenado.map((proposicao) => ({
        id: proposicao.IdentificacaoMateria.CodigoMateria,
        siglaTipo: proposicao.IdentificacaoMateria.SiglaSubtipoMateria,
        origem: 'Senado Federal',
        dataApresentacao: new Date(proposicao.DadosBasicosMateria.DataApresentacao),
        situacao: proposicao.SituacaoAtual.Autuacoes.Autuacao.Situacao.DescricaoSituacao,
        numero: proposicao.IdentificacaoMateria.NumeroMateria,
        ano: proposicao.IdentificacaoMateria.AnoMateria,
        ementa: proposicao.DadosBasicosMateria.EmentaMateria,
        autor: getAutorAleatorio(proposicao),
        casa: {
            id: 2,
            nome: 'Senado Federal'
        }
    }));

    /**
     * Camara
     */

    const proposicoes = await httpGetCamara(getHousesParams(1, params))
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

    let propsCamara = proposicoes.map((proposicao, index) => ({
        id: proposicao.id,
        dataApresentacao: new Date(get(proposicoesDetalhes[index], 'dataApresentacao')),
        siglaTipo: proposicao.siglaTipo,
        origem: 'Câmara dos Deputados',
        numero: proposicao.numero,
        situacao: get(proposicoesDetalhes[index], 'statusProposicao.descricaoSituacao'),
        ano: proposicao.ano,
        ementa: proposicao.ementa,
        autor: autoresComFotos[index],
        casa: {
            id: 1,
            nome: 'Câmara dos Deputados'
        }
    }))

    return [...propsCamara, ...propsSenado]
}
