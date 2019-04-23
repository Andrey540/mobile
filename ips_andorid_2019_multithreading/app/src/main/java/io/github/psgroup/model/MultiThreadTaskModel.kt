package io.github.psgroup.model

import java.util.concurrent.Executors

class MultiThreadTaskModel {
    private val mTaskMap = mutableMapOf<String, TaskModel>()
    private val executor = Executors.newCachedThreadPool()

    fun start(id: String, presenter: TaskModel.IPresenter) {
        if (!mTaskMap.contains(id)) {
            mTaskMap[id] = TaskModel()
        }
        if (mTaskMap[id]!!.isReadyToRun()) {
            mTaskMap[id]!!.start(executor, presenter)
        }
    }

    fun subscribe(id: String, presenter: TaskModel.IPresenter) {
        mTaskMap[id]?.subscribe(presenter)
    }

    fun unsubscribe(id: String) {
        mTaskMap[id]?.unsubscribe()
    }
}