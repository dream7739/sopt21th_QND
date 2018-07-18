package org.sopt21.qnd.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import org.sopt21.qnd.R

class FragmentStore : Fragment() {
    private lateinit var img: ImageView
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_store, container, false)

        img = view.findViewById(R.id.frag_store) as ImageView

        img.setOnClickListener{
            Toast.makeText(context, "구현예정", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
