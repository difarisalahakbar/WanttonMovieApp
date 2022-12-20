package com.difa.myapplication.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    private var mBinding: T? = null
    protected val binding
        get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = getViewBinding(inflater, container)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupIntent()
        setupUI()
        setupAction()
        setupProcess()
        setupObserver()
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): T

    abstract fun setupIntent()

    abstract fun setupUI()

    abstract fun setupAction()

    abstract fun setupProcess()

    abstract fun setupObserver()

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}