package com.yap.yappk.pk.utils.pagination;

import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup

internal class PaginatedSpanSizeLookup(
    val originalLookup: SpanSizeLookup,
    spanCount: Int,
    adapter: PaginatedAdapter
) : SpanSizeLookup() {
    private val fakeLookup: SpanSizeLookup
    private val adapter: PaginatedAdapter
    override fun getSpanSize(position: Int): Int {
        return if (adapter.isPlaceholderRow(position) || adapter.isErrorRow(position)) fakeLookup.getSpanSize(
            position
        ) else originalLookup.getSpanSize(position)
    }

    init {
        fakeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return spanCount
            }
        }
        this.adapter = adapter
    }
}