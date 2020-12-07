package app.igormatos.botaprarodar.utils

import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.model.community.CommunityBody
import app.igormatos.botaprarodar.domain.model.community.CommunityResponse

fun communityResponseStub() =
    CommunityResponse(
        "Rua Teste, 123",
        12345678,
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

fun completeCommunityBodyStub() =
    CommunityBody(
        name = "Teste",
        description = "Descrição teste",
        address = "Rua Teste, 123",
        orgName = "Nome Teste",
        orgEmail = "teste@tste.com"
    )

fun nullCommunityResponseItemStub() =
    CommunityResponse(
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
