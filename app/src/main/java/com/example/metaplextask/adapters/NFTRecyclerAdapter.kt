package com.example.metaplextask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.metaplextask.R
import com.example.metaplextask.services.MetaplexService
import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.nfts.models.NFT

class NFTRecyclerAdapter(
    private val context: FragmentActivity?,
    private var nftList: ArrayList<NFT>,
    private val metaplex: Metaplex
): RecyclerView.Adapter<NFTRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nftImage: ImageView
        val nftName: TextView
        val nftMint: TextView

        init {
            nftImage = view.findViewById(R.id.nft_image)
            nftName = view.findViewById(R.id.nft_name_textview)
            nftMint = view.findViewById(R.id.nft_mint_textview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.nft_cardview, parent, false)
        return ViewHolder(view)
    }

    //I'm way more familiar with C# multithreading. Did this to prevent data coming in from multiple threads
    @Synchronized override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //If I had more time I would add a hashmap to check if position is already there.
        //Then get from hashmap instead of making separate http request. If not in hashmap then do http request.
        MetaplexService.getNFTImageURI(metaplex, nftList[position]){
            context?.runOnUiThread {
                holder.nftName.text = nftList[position].name
                holder.nftMint.text = nftList[position].mint.toString()
                Glide.with(context)
                    .load(it)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into(holder.nftImage)
            }
        }
    }

    override fun getItemCount(): Int {
        return nftList.size
    }
}