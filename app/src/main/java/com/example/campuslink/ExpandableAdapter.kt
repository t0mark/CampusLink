package com.example.campuslink

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpandableAdapter(
    private var visibleNodes: MutableList<Node>, // 현재 화면에 표시할 노드 리스트 받아옴
    private val onLeafClick: (Node) -> Unit
) : RecyclerView.Adapter<ExpandableAdapter.NodeViewHolder>() {

    inner class NodeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.categoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_parent, parent, false)
        return NodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
        val node = visibleNodes[position]

        holder.title.text = node.name

        // 들여쓰기
        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginStart = node.level * 100 // 레발당 100
        holder.itemView.layoutParams = layoutParams

        // 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            if (node.children == null) {//리프인가?
                onLeafClick(node)
            } else {
                toggleNode(node, position)
            }
        }
    }

    override fun getItemCount(): Int = visibleNodes.size

    private fun toggleNode(node: Node, position: Int) {
        if (node.isExpanded) {//열려 있으면 닫기.
            collapseNode(node, position)
        } else {
            expandNode(node, position)
        }
    }

    private fun expandNode(node: Node, position: Int) {

        val parentLevel = node.level
        var i = position + 1

        //확장을 위해 자리 만듬
        while (i < visibleNodes.size && visibleNodes[i].level > parentLevel) {
            visibleNodes.removeAt(i)
        }
        notifyDataSetChanged()//갱신

        //확장
        node.isExpanded = true
        val children = node.children ?: return
        visibleNodes.addAll(position + 1, children)//리스트에 추가
        notifyItemRangeInserted(position + 1, children.size)
        notifyItemChanged(position)//갱신
    }




    private fun collapseNode(node: Node, position: Int) {
        node.isExpanded = false
        val childrenCount = countChildren(node)

        // 자식 노드를 제거
        for (i in 0 until childrenCount) {
            visibleNodes.removeAt(position + 1)
        }
        notifyItemRangeRemoved(position + 1, childrenCount)
        notifyItemChanged(position)
    }

    private fun countChildren(node: Node): Int {//닫기 위해서 자식들 다 찾기
        if (node.children == null) return 0
        var count = 0
        for (child in node.children) {
            count++
            if (child.isExpanded) {
                count += countChildren(child)
            }
        }
        return count
    }

    //root 업데이트
    fun updateRoot(targetNodes: List<Node>) {
        visibleNodes.clear()
        visibleNodes.addAll(targetNodes)
        notifyDataSetChanged()
    }
}
