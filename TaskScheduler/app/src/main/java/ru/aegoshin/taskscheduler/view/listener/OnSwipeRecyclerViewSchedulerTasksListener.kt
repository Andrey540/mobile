package ru.aegoshin.taskscheduler.view.listener

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import ru.aegoshin.taskscheduler.R
import ru.aegoshin.taskscheduler.view.adapter.TaskListViewRecyclerAdapter

class OnSwipeRecyclerViewSchedulerTasksListener(
    context: Context,
    adapter: TaskListViewRecyclerAdapter,
    dragDirs: Int,
    swipeDirs: Int
) :
    OnSwipeRecyclerViewListener(context, adapter, dragDirs, swipeDirs) {

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        decorateListItem(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val maxSwipe = viewHolder.itemView.height * MAX_SWIPE_COEFFICIENT
        var translationX = dX
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX <= 0) {
            translationX = -minOf(-dX, maxSwipe)
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX > 0) {
            translationX = minOf(dX, maxSwipe * 2)
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            var left: Float = viewHolder.itemView.left.toFloat()
            val right: Float = if (dX > 0) (left + maxSwipe * 2) else viewHolder.itemView.right.toFloat()
            left = if (dX > 0) left else right - maxSwipe
            val top: Float = viewHolder.itemView.top.toFloat()
            val bottom: Float = top + maxSwipe

            recyclerView.setOnTouchListener(getOnTouchListener(viewHolder, actionState, dX, left, right, top, bottom))
        }

        onChildDrawImpl(canvas, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive)
    }

    override fun decorateListItem(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.decorateListItem(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        if (dX > 0) {
            val background = ColorDrawable(ContextCompat.getColor(context, R.color.editTaskColor))
            background.setBounds(
                viewHolder.itemView.height * MAX_SWIPE_COEFFICIENT.toInt(),
                viewHolder.itemView.top,
                viewHolder.itemView.left + dX.toInt(),
                viewHolder.itemView.bottom
            )
            background.draw(canvas)

            val icon = ContextCompat.getDrawable(context, R.drawable.ic_swap)
            val iconSize = icon!!.intrinsicHeight
            val halfIcon = iconSize / 2
            val margin =
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ICON_MARGIN.toFloat(),
                    context.resources.displayMetrics
                ).toInt()
            val top = viewHolder.itemView.top + ((viewHolder.itemView.bottom - viewHolder.itemView.top) / 2 - halfIcon)
            icon.setBounds(
                viewHolder.itemView.height * MAX_SWIPE_COEFFICIENT.toInt() + margin,
                top,
                viewHolder.itemView.height * MAX_SWIPE_COEFFICIENT.toInt() + margin + icon.intrinsicWidth,
                top + icon.intrinsicHeight
            )
            icon.draw(canvas)
        }
    }

    private fun getOnTouchListener(
        viewHolder: RecyclerView.ViewHolder,
        actionState: Int,
        dX: Float,
        left: Float,
        right: Float,
        top: Float,
        bottom: Float
    ): View.OnTouchListener {
        return View.OnTouchListener { _, motionEvent ->
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE &&
                motionEvent.action == MotionEvent.ACTION_DOWN &&
                motionEvent.x >= left && motionEvent.x <= right &&
                motionEvent.y <= bottom && motionEvent.y >= top
            ) {
                if (dX > 0) {
                    if (motionEvent.x > viewHolder.itemView.height * MAX_SWIPE_COEFFICIENT.toInt()) {
                        adapter.onSwapTaskStatus(viewHolder.adapterPosition)
                    } else {
                        adapter.onEditTask(viewHolder.adapterPosition)
                    }
                } else {
                    adapter.onDeleteTask(viewHolder.adapterPosition)
                }
            }
            return@OnTouchListener false
        }
    }

    companion object {
        private const val MAX_SWIPE_COEFFICIENT = 1.0f
        private const val ICON_MARGIN = 16
    }
}