package classes

import info.but1.sae2025.QuiEstCeClient

class Adversaire(
    client: QuiEstCeClient,
    nom: String,
    prenom: String,
    id: Int,
) : Identite(client, nom, prenom, id)