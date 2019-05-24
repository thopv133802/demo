package com.thopham.projects.desktop.demo.common

import io.reactivex.Scheduler
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import io.reactivex.schedulers.Schedulers


val UI: JavaFxScheduler = JavaFxScheduler.platform()
val IO: Scheduler = Schedulers.io()
val COMPUTATION: Scheduler = Schedulers.computation()