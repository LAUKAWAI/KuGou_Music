package com.example.kugouactivity

import android.app.Activity
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_item.*

class MainActivity : AppCompatActivity() {

    private var index = 0
    private val itemList = ArrayList<ItemData>()
    private val mediaParser = MediaPlayer()

    private fun initItem() {
        repeat(1) {
            itemList.add(ItemData("阿乐", "许廷铿", R.drawable.music_01, "music_01.mp3"))
            itemList.add(ItemData("残忍", "许廷铿", R.drawable.music_02, "music_02.mp3"))
            itemList.add(ItemData("对白", "许廷铿", R.drawable.music_03, "music_03.mp3"))
            itemList.add(ItemData("护航", "许廷铿", R.drawable.music_04, "music_04.mp3"))
            itemList.add(ItemData("蚂蚁", "许廷铿", R.drawable.music_05, "music_05.mp3"))
            itemList.add(ItemData("面具", "许廷铿", R.drawable.music_06, "music_06.mp3"))
            itemList.add(ItemData("仁至义尽", "许廷铿", R.drawable.music_07, "music_07.mp3"))
            itemList.add(ItemData("如你是我", "许廷铿", R.drawable.music_08, "music_08.mp3"))
            itemList.add(ItemData("四季予你", "程响", R.drawable.music_09, "music_09.mp3"))
            itemList.add(ItemData("登对", "许廷铿", R.drawable.music_10, "music_10.mp3"))
            itemList.add(ItemData("时光", "许廷铿", R.drawable.music_11, "music_11.mp3"))
            itemList.add(ItemData("蜗居", "许廷铿", R.drawable.music_12, "music_12.mp3"))
            itemList.add(ItemData("遗物", "许廷铿", R.drawable.music_13, "music_13.mp3"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        music_name
        fullScreen(this)
        initItem()
        initMediaPlayer(itemList[index].music)
        //设置线性布局管理器到控件
        val layoutManager = LinearLayoutManager(this)
        RecyclerViewTest.layoutManager = layoutManager
        val adapter = ItemAdapter(itemList)
        RecyclerViewTest.adapter = adapter


        musicPlay.setOnClickListener {
            if (!mediaParser.isPlaying) {
                musicPlay.setImageResource(R.drawable.start)
                mediaParser.start()
            } else {
                musicPlay.setImageResource(R.drawable.ic_bo)
                mediaParser.pause()
            }
        }

        nextSong.setOnClickListener {
            index++
            mediaParser.reset()
            if (index > itemList.size - 1) {
                index = 0
            }
            musicManager()
        }

        lastSong.setOnClickListener {
            index--
            mediaParser.reset()
            if (index < 0) {
                index = itemList.size - 1
            }
            musicManager()
        }
    }

    private fun rotate(int: Int) {
        val translateAnimation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        translateAnimation.duration = 10000
        translateAnimation.repeatCount = int / 10000
        translateAnimation.repeatMode = 1
        cardView.animation = translateAnimation
        translateAnimation.start()
    }

    private fun musicManager() {
        initMediaPlayer(itemList[index].music)
        textView2.text = itemList[index].title
        textView3.text = itemList[index].content
        imageView11.setImageResource(itemList[index].image)
        mediaParser.start()
        musicPlay.setImageResource(R.drawable.start)
    }


    inner class ItemAdapter(private val itemList: ArrayList<ItemData>) :
        RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.findViewById(R.id.music_name)
            val itTitle: TextView = view.findViewById(R.id.music_author)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
            val viewHolder = ViewHolder(view)
            viewHolder.itemView.setOnClickListener {
                //获取位置
                index = viewHolder.adapterPosition
                //下标
                val list = itemList[index]
                mediaParser.reset()
                initMediaPlayer(list.music)
                musicPlay.setImageResource(R.drawable.start)
                mediaParser.start()
                textView2.text = list.title
                textView3.text = list.content
                imageView11.setImageResource(list.image)

            }
            return viewHolder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = itemList[position]
            holder.title.text = item.title
            holder.itTitle.text = item.content
        }

        override fun getItemCount() = itemList.size
    }

    private fun initMediaPlayer(music: String) {
        val assetManager = assets
        val fd = assetManager.openFd(music)
        mediaParser.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        mediaParser.prepare()
        rotate(mediaParser.duration)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaParser.stop()
        mediaParser.release()
    }

    /**
     * 通过设置全屏，设置状态栏透明
     */
    private fun fullScreen(activity: Activity) {
        run {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            val window = activity.window
            val decorView = window.decorView
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            decorView.systemUiVisibility = option
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}


