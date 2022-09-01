package com.example.tedy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.tedy.bean.SearchBlock
import com.example.tedy.bean.SearchType
import com.example.tedy.serchMethod.SearchMethod
import com.example.tedy.serchMethod.SearchMethodForAccessibility
import com.example.tedy.serchMethod.SearchMethodForUrlScheme
import com.example.tedy.util.FileGet
import com.example.tedy.util.FileGet.bm
import com.example.tedy.util.FileGet.getFile
import com.example.tedy.util.FileGet.ib

import com.example.tedy.util.My_AndroidUtil
import com.example.tedy.util.Search_RecyclerViewAdapter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.pow
import kotlin.math.sin





class SettingBlock : Fragment() {
    private var search_recyclerViewAdapter: Search_RecyclerViewAdapter? = null

    var currSearchBlock : SearchBlock? = null
    private var settingBlock: MaterialCardView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileGet?.settingBlock = this





    }
    fun set_Search_recyclerViewAdapter(se : Search_RecyclerViewAdapter){

        if (search_recyclerViewAdapter == null){
            search_recyclerViewAdapter = se
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("varIF Exisy@", "onViewCreated$search_recyclerViewAdapter: ")
        FileGet.macView = view


        settingBlock = view.findViewById(R.id.settingBlock)
        readSettingInput(view)
        view.findViewById<Button>(R.id.close_Bth).setOnClickListener{
            Log.d("TAG", "onViewCre1ated: ${settingBlock}")
            scaleCardClose()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }



    companion object {

    }

    fun WriteSettingInput(view:View) {
        val searchName: TextInputEditText? = view.findViewById(R.id.SearchName)
        val input_JsonStr: TextInputEditText? = view.findViewById(R.id.input_JsonStr)
        val input_urlScheme: TextInputEditText? =
            view.findViewById(R.id.input_urlScheme)
        val input_type: TextInputEditText? = view.findViewById(R.id.type)
        searchName?.setText(currSearchBlock?.searchName)
        input_JsonStr?.setText(currSearchBlock?.jsonStr)
        input_urlScheme?.setText(currSearchBlock?.urlScheme)
        val oldType: SearchType? = currSearchBlock?.type
        if (oldType == SearchType.ACCESSIBILITY) {
            input_type?.setText("A")
        } else if (oldType == SearchType.URL_SCHEME) {
            input_type?.setText("U")
        }
        view.findViewById<ImageButton>(R.id.addSearchBlock)?.setOnClickListener {
            var type: SearchType? = null
            var searchMethod: SearchMethod? = null
            if (input_type?.text.toString() == "U") {
                type = SearchType.URL_SCHEME
                searchMethod = SearchMethodForUrlScheme()
            } else if (input_type?.text.toString() == "A") {
                type = SearchType.ACCESSIBILITY
                searchMethod = SearchMethodForAccessibility()
            }
            val searchBlock = SearchBlock(
                searchName?.text.toString(),
                input_urlScheme?.text.toString(),
                input_JsonStr?.text.toString(),
                type
            )
            searchBlock.searchMethod = searchMethod
            if (bm != null) {
                searchBlock.apkIcon = My_AndroidUtil.bitmapToByte(bm)
            }
            Log.d("FuckBug", "onCreate: " + input_JsonStr?.text.toString())
            search_recyclerViewAdapter?.remove(currSearchBlock)
            search_recyclerViewAdapter?.add(searchBlock)
            scaleCardClose()
        }
        view.findViewById<Button>(R.id.remove_Bth)?.setOnClickListener {
            search_recyclerViewAdapter?.remove(currSearchBlock)
            scaleCardClose()
        }
    }
    fun readSettingInput(view: View) {
        val mac = FileGet.getMac()

        val searchName: TextInputEditText? = view.findViewById(R.id.SearchName)
        val input_JsonStr: TextInputEditText? = view.findViewById<TextInputEditText>(R.id.input_JsonStr)
        val input_urlScheme: TextInputEditText? =
            view.findViewById<TextInputEditText>(R.id.input_urlScheme)
        val input_type: TextInputEditText? = view.findViewById<TextInputEditText>(R.id.type)
        val addSearchBlock: ImageButton? = view.findViewById<ImageButton>(R.id.addSearchBlock)

        //读取输入框并构建一个SearchBlock 然后添加到search_recyclerViewAdapter里
        addSearchBlock?.setOnClickListener {
            try {
                var type: SearchType? = null
                var searchMethod: SearchMethod? = null
                if (input_type?.text.toString() == "U") {
                    type = SearchType.URL_SCHEME
                    searchMethod = SearchMethodForUrlScheme()
                } else if (input_type?.text.toString() == "A") {
                    type = SearchType.ACCESSIBILITY
                    searchMethod = SearchMethodForAccessibility()
                }
                val searchBlock = SearchBlock(
                    searchName?.text.toString(),
                    input_urlScheme?.text.toString(),
                    input_JsonStr?.text.toString(),
                    type
                )
                Log.e("SETTING", "readSettingInput${searchBlock.type}: ")
                searchBlock.searchMethod = searchMethod
                if (bm != null) {
                    searchBlock.apkIcon = My_AndroidUtil.bitmapToByte(bm)
                }
                Log.d("FuckBug", "onCreate: " + input_JsonStr?.text.toString())
                search_recyclerViewAdapter?.add(searchBlock)
                scaleCardClose()
                view.findViewById<View>(R.id.remove_Bth)?.setOnClickListener {}
            } catch (e: Exception) {
//                Toast.makeText(this@MainActivity, "不能为空或者类型不对", Toast.LENGTH_LONG).show()
            }
        }
        val setImageButtom: ImageButton? = view.findViewById<ImageButton>(R.id.setImageButtom)
        //设置图片到当前的iamgebutton 传递给fileGet 在resultApi接受 设置给对应的按钮
        setImageButtom?.setOnClickListener {
            ib = setImageButtom
            getFile()
        }
    }

    fun scaleCard() {
        settingBlock?.animate()?.apply {
            duration = 800
            scaleX(1f)
            scaleY(1f)
            interpolator = Interpolator { x: Float ->
                val factor = 0.4.toFloat()
                val v = (2.0.pow((-10 * x).toDouble()) * sin((x - factor / 4) * (2 * 3.14) / factor) + 1).toFloat()
                v
            }
        }?.start()
    }
    fun scaleCardClose() {
        settingBlock?.animate()?.apply {
            duration = 800
            scaleX(0f)
            scaleY(0f)
            interpolator = Interpolator { x: Float ->
                val factor = 0.95.toFloat()
                val v = (2.0.pow((-10 * x).toDouble()) * sin((x - factor / 4) * (2 * 3.14) / factor) + 1).toFloat()
                v
            }
        }?.start()
    }

}