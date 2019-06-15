package ru.aegoshin.taskscheduler.view.listener

import android.content.Context
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.TypedValue
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import ru.aegoshin.taskscheduler.R
import android.view.MotionEvent
import android.view.View
import ru.aegoshin.taskscheduler.view.adapter.TaskListViewRecyclerAdapter

open class OnSwipeRecyclerViewListener(
    protected val context: Context,
    protected val adapter: TaskListViewRecyclerAdapter,
    dragDirs: Int,
    swipeDirs: Int
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    protected val swipedItems = mutableMapOf<RecyclerView.ViewHolder, Int>()

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
        swipedItems[viewHolder] = direction
        adapter.onSwipe(viewHolder.adapterPosition)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        swipedItems.remove(viewHolder)
    }

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
            translationX = minOf(dX, maxSwipe)
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            recyclerView.setOnTouchListener(getOnTouchListener())
        }

        onChildDrawImpl(canvas, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive)
    }

    protected fun onChildDrawImpl(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    protected open fun decorateListItem(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            context,
            canvas,
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
    }

    protected fun getViewHolderByCoordinates(y: Float): RecyclerView.ViewHolder? {
        val items = swipedItems.filter { it.key.itemView.top.toFloat() <= y && it.key.itemView.bottom.toFloat() >= y }
        return if (items.isEmpty()) null else items.toList()[0].first
    }

    protected fun getBlockSize(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            MAX_SWIPE_SIZE.toFloat(),
            context.resources.displayMetrics
        )
    }

    private fun getOnTouchListener(
    ): View.OnTouchListener {
        return View.OnTouchListener { _, motionEvent ->
            val viewHolder = getViewHolderByCoordinates(motionEvent.y)
            if (viewHolder != null) {
                val blockSize = getBlockSize()
                val direction = swipedItems[viewHolder]
                var left: Float = viewHolder.itemView.left.toFloat()
                val right: Float = if (direction == ItemTouchHelper.RIGHT) (left + blockSize) else viewHolder.itemView.right.toFloat()
                left = if (direction == ItemTouchHelper.RIGHT) left else right - blockSize
                if (motionEvent.action == MotionEvent.ACTION_DOWN &&
                    motionEvent.x >= left && motionEvent.x <= right
                ) {
                    if (direction == ItemTouchHelper.RIGHT) {
                        adapter.onEditTask(viewHolder.adapterPosition)
                    } else if (direction == ItemTouchHelper.LEFT) {
                        adapter.onDeleteTask(viewHolder.adapterPosition)
                    }
                }
            }
            return@OnTouchListener false
        }
    }

    companion object {
        private const val MAX_SWIPE_SIZE = 56
    }
}