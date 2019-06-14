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

        val maxSwipe = getBlockSize()
        var translationX = dX
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX <= 0) {
            translationX = -minOf(-dX, maxSwipe)
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX > 0) {
            translationX = minOf(dX, maxSwipe * 2)
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            recyclerView.setOnTouchListener(getOnTouchListener())
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

        val blockSize = getBlockSize()
        if (dX > 0) {
            val background = ColorDrawable(ContextCompat.getColor(context, R.color.editTaskColor))
            background.setBounds(
                blockSize.toInt(),
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
                blockSize.toInt() + margin,
                top,
                blockSize.toInt() + margin + icon.intrinsicWidth,
                top + icon.intrinsicHeight
            )
            icon.draw(canvas)
        }
    }

    private fun getOnTouchListener(
    ): View.OnTouchListener {
        return View.OnTouchListener { _, motionEvent ->
            val viewHolder = getViewHolderByCoordinates(motionEvent.y)
            if (viewHolder != null) {
                val blockSize = getBlockSize()
                val direction = swipedItems[viewHolder]
                var left: Float = viewHolder.itemView.left.toFloat()
                val right: Float = if (direction == ItemTouchHelper.RIGHT) (left + blockSize * 2) else viewHolder.itemView.right.toFloat()
                left = if (direction == ItemTouchHelper.RIGHT) left else right - blockSize
                if (motionEvent.action == MotionEvent.ACTION_DOWN &&
                    motionEvent.x >= left && motionEvent.x <= right
                ) {
                    if (direction == ItemTouchHelper.RIGHT) {
                        if (motionEvent.x > getBlockSize()) {
                            adapter.onSwapTaskStatus(viewHolder.adapterPosition)
                        } else {
                            adapter.onEditTask(viewHolder.adapterPosition)
                        }
                    } else if (direction == ItemTouchHelper.LEFT) {
                        adapter.onDeleteTask(viewHolder.adapterPosition)
                    }
                }
            }
            return@OnTouchListener false
        }
    }

    companion object {
        private const val ICON_MARGIN = 16
    }
}