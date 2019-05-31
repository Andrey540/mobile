package ru.aegoshin.taskscheduler.view.listener

import android.content.Context
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import ru.aegoshin.taskscheduler.R
import android.view.MotionEvent
import android.view.View
import ru.aegoshin.taskscheduler.view.adapter.TaskListViewRecyclerAdapter


open class OnSwipeRecyclerViewListener(
    private val context: Context,
    private val adapter: TaskListViewRecyclerAdapter,
    dragDirs: Int,
    swipeDirs: Int
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    private val swipedItems = mutableMapOf<RecyclerView.ViewHolder, Boolean>()

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.1f
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (swipedItems.contains(viewHolder)) {
            swipedItems.remove(viewHolder)
            adapter.notifyItemChanged(viewHolder.adapterPosition)
        } else {
            swipedItems[viewHolder] = true
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            context,
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.red))
            .addSwipeLeftActionIcon(R.drawable.ic_trash)
            .addSwipeRightBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .addSwipeRightActionIcon(R.drawable.ic_edit)
            .create()
            .decorate()

        val maxSwipe = viewHolder.itemView.height * MAX_SWIPE_COEFFICIENT
        var translationX = dX
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX <= 0)
        {
            translationX = -minOf(-dX, maxSwipe)
        }
        else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX > 0)
        {
            translationX = minOf(dX, maxSwipe)
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            var left: Float = viewHolder.itemView.left.toFloat()
            val right: Float = if (dX > 0) (left + maxSwipe) else viewHolder.itemView.right.toFloat()
            left = if (dX > 0) left else right - maxSwipe
            val top = viewHolder.itemView.top
            val bottom = top + maxSwipe

            recyclerView.setOnTouchListener(View.OnTouchListener { _, motionEvent ->
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE &&
                    motionEvent.action == MotionEvent.ACTION_DOWN &&
                    motionEvent.x >= left && motionEvent.x <= right &&
                    motionEvent.y <= bottom && motionEvent.y >= top
                ) {
                    if (dX > 0) {
                        adapter.onEditTask(viewHolder.adapterPosition)
                    }
                    else {
                        adapter.onDeleteTask(viewHolder.adapterPosition)
                    }
                }
                return@OnTouchListener false
            })
        }

        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive)
    }

    companion object {
        private const val MAX_SWIPE_COEFFICIENT = 1.3f
    }
}