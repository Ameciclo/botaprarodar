package app.igormatos.botaprarodar.utils

import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.model.community.CommunityRequest

fun communityResponseStub() =
    CommunityRequest(
        "Rua Teste, 123",
        "2020-01-01",
        "Teste descricão",
        "1",
        "Teste",
        "teste@teste.com",
        "Nome Teste"
    )


fun communityListResponseStub() =
    listOf(
        communityResponseStub(),
        communityResponseStub()
    )

fun communityMapResponseStub() =
    mapOf(
        Pair("1", communityResponseStub()),
        Pair("2", communityResponseStub())
    )

fun mappedCommunityStub() =
    Community(
        "Teste",
        "Teste descrição",
        "Rua Teste, 123",
        "Nome Teste",
        "teste@teste.com",
        "1"
    )

fun mappedCommunityListStub() =
    listOf(
        mappedCommunityStub(),
        mappedCommunityStub()
    )

fun completeCommunityRequestStub() =
    CommunityRequest(
        name = "Teste",
        description = "Descrição teste",
        address = "Rua Teste, 123",
        orgName = "Nome Teste",
        orgEmail = "teste@tste.com",
        createdDate = "2022-01-01",
        id = null
    )

fun nullCommunityResponseItemStub() =
    CommunityRequest(
        address = null,
        description = null,
        name = null,
        orgEmail = null,
        orgName = null,
        id = null,
        createdDate = null
    )

fun nullCommunityResponseItemListStub() =
    listOf(
        nullCommunityResponseItemStub(),
        nullCommunityResponseItemStub()
    )

fun nullCommunityResponseItemMapStub() =
    mapOf(
        Pair("1", nullCommunityResponseItemStub()),
        Pair("2", nullCommunityResponseItemStub())
    )
