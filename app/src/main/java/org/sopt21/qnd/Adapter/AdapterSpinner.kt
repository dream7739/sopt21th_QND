package org.sopt21.qnd

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AdapterSpinner(internal var context: Context, internal var data: List<String>?) : BaseAdapter() {
    internal var inflater: LayoutInflater

    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return if (data != null)
            data!!.size
        else
            0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_normal, parent, false)
        }

        if (data != null) {
            //데이터세팅
            val text = data!![position]
            (convertView!!.findViewById(R.id.spinnerText) as TextView).text = text
        }

        return convertView!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_dropdown, parent, false)
        }

        //데이터세팅
        val text = data!![position]
        (convertView!!.findViewById(R.id.spinnerText) as TextView).text = text

        return convertView
    }

    override fun getItem(position: Int): Any {
        return data!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
