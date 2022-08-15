package com.example.metaplextask.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.metaplextask.adapters.NFTRecyclerAdapter
import com.example.metaplextask.databinding.FragmentMainBinding
import com.example.metaplextask.services.MetaplexService
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.storage.OkHttpSharedStorageDriver
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.solana.SolanaConnectionDriver
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var nftList: ArrayList<NFT>
    private lateinit var adapter: NFTRecyclerAdapter
    private val publicKey = PublicKey("4oX431PqGrFBk2qrKQeUMZzabuwsP9AUWfpFbK3vxsLU")

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val metaplex: Metaplex
        val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
        val solanaIdentityDriver = ReadOnlyIdentityDriver(publicKey, solanaConnection.solanaRPC)
        val storageDriver = OkHttpSharedStorageDriver()
        metaplex = Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)

        nftList = arrayListOf()
        adapter = NFTRecyclerAdapter(activity, nftList, metaplex)
        binding.nftDetailsRecyclerview.layoutManager = GridLayoutManager(context, 2)
        binding.nftDetailsRecyclerview.adapter = adapter

        MetaplexService.getNFTData(publicKey, metaplex) {
            nftList.addAll(ArrayList(it))
            requireActivity().runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }

        binding.goButton.setOnClickListener {
            MetaplexService.getNFTData(PublicKey(binding.publicKeyEdittext.text.toString()), metaplex) {
                nftList.clear()
                nftList.addAll(ArrayList(it))
                requireActivity().runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}