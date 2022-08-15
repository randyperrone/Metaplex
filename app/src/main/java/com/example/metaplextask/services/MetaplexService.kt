package com.example.metaplextask.services

import android.util.Log
import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.nfts.models.NFT
import com.solana.core.PublicKey

object MetaplexService {

    fun getNFTData(publicKey: PublicKey, metaplex: Metaplex, callback: (nftList: List<NFT>) -> Unit) {
        metaplex.nft.findAllByOwner(publicKey) { result ->
            result.onSuccess { nft ->
                val nftList = nft.filterNotNull()
                callback(nftList)
            }.onFailure {
                Log.e("MetaplexService", "Failed Lookup")
            }
        }
    }

    fun getNFTImageURI(metaplex: Metaplex, nft: NFT, callback: (nftImageURI: String?) -> Unit) {
        nft.metadata(metaplex) {result ->
                result.onSuccess {
                    callback(it.image)
                }
        }
    }
}