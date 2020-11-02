package com.celerocommerce.handylist.repositories

import kotlinx.coroutines.Job
import timber.log.Timber

abstract class JobManager(
    private val className: String
) {

    private val jobs: HashMap<String, Job> = HashMap()

    fun addJob(methodName: String, job: Job) {
        cancelJob(methodName)
        jobs[methodName] = job
    }

    private fun cancelJob(methodName: String) {
        Timber.d("$className: cancelling job in method: $methodName")
        getJob(methodName)?.cancel()
    }

    private fun getJob(methodName: String): Job? {
        if(jobs.containsKey(methodName)) {
            return jobs[methodName]
        }
        return null
    }

    fun cancelActiveJobs() {
        Timber.d("$className cancelling all active jobs")
        for ((methodName, job) in jobs) {
            if (job.isActive) {
                Timber.d("$className: cancelling job in method: $methodName")
                job.cancel()
            }
        }
    }
}