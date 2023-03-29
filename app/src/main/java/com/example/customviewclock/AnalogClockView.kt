package com.example.customviewclock

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.lang.Math.PI
import java.util.*
import kotlin.math.cos
import kotlin.math.sin


class AnalogClockView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

	private var mHeight = 0
	private var mWidth = 0
	private var mPadding = 0
	private var mHandTruncation = 0
	private var mHourHandTruncation = 0
	private var mRadius = 0
	private var mPaint: Paint? = null
	private var mIsInit = false
	private val numbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
	private var mMinimum = 0
	private lateinit var mRect: Rect

	private fun initClock() {
		mHeight = height
		mWidth = width
		mPadding = 80
		mMinimum = mHeight.coerceAtMost(mWidth)
		mRadius = mMinimum / 2 - mPadding
		mHandTruncation = mMinimum / 8
		mHourHandTruncation = mMinimum / 7
		mIsInit = true
		mPaint = Paint()
		mRect = Rect()
	}

	override fun onDraw(canvas: Canvas?) {
		super.onDraw(canvas)
		if (!mIsInit) {
			initClock()
		}
		drawCircle(canvas!!)
		drawMarks(canvas)
		drawHands(canvas)
		postInvalidateDelayed(500)
		invalidate()
	}

	private fun setPaintAttributes(colour: Int, stroke: Paint.Style, strokeWidth: Int) {
		mPaint?.reset()
		mPaint?.color = colour
		mPaint?.style = stroke
		mPaint?.strokeWidth = strokeWidth.toFloat()
		mPaint?.isAntiAlias = true
	}

	private fun drawCircle(canvas: Canvas) {
		mPaint?.reset()
		setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 16)
		canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (mRadius).toFloat(), mPaint!!)
	}

	private fun drawMarks(canvas: Canvas) {
		mPaint?.reset()
		setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 16)
		for (number in numbers) {
			val angle = PI / 6 * (number - 3)
			canvas.drawLine(
				(width / 2 + cos(angle) * mRadius).toInt().toFloat(),
				(height / 2 + sin(angle) * mRadius).toInt().toFloat(),
				(width / 2 + cos(angle) * mRadius * 0.9).toInt().toFloat(),
				(height / 2 + sin(angle) * mRadius * 0.9).toInt().toFloat(),
				mPaint!!
			)
		}
	}

	private fun drawSecondsHand(canvas: Canvas, loc: Float) {
		mPaint?.reset()
		setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 18)
		val angle = PI * loc / 30 - PI / 2
		val handLength: Int = mRadius - mHandTruncation
		canvas.drawLine(
			(width / 2 - cos(angle) * handLength / 3).toFloat(),
			(height / 2 - sin(angle) * handLength / 3).toFloat(),
			(width / 2 + cos(angle) * handLength).toFloat(),
			(height / 2 + sin(angle) * handLength).toFloat(),
			mPaint!!
		)
	}

	private fun drawMinuteHand(canvas: Canvas, loc: Float) {
		mPaint?.reset()
		setPaintAttributes(Color.RED, Paint.Style.STROKE, 16)
		val angle = PI * loc / 30 - PI / 2
		val handRadius: Int = mRadius - mHandTruncation
		canvas.drawLine(
			(width / 2 - cos(angle) * handRadius / 3).toFloat(),
			(height / 2 - sin(angle) * handRadius / 3).toFloat(),
			(width / 2 + cos(angle) * handRadius / 1.5).toFloat(),
			(height / 2 + sin(angle) * handRadius / 1.5).toFloat(),
			mPaint!!
		)
	}

	private fun drawHourHand(canvas: Canvas, loc: Float) {
		mPaint?.reset()
		setPaintAttributes(Color.BLUE, Paint.Style.STROKE, 10)
		val angle = PI * loc / 30 - PI / 2
		val handRadius: Int = mRadius - mHandTruncation * 2
		canvas.drawLine(
			(width / 2 - cos(angle) * handRadius / 2).toFloat(),
			(height / 2 - sin(angle) * handRadius / 2).toFloat(),
			(width / 2 + cos(angle) * handRadius).toFloat(),
			(height / 2 + sin(angle) * handRadius).toFloat(),
			mPaint!!
		)
	}

	private fun drawHands(canvas: Canvas) {
		val calendar = Calendar.getInstance()
		var hour = calendar[Calendar.HOUR_OF_DAY].toFloat()
		hour = if (hour > 12) hour - 12 else hour
		drawHourHand(canvas, (hour + calendar[Calendar.MINUTE] / 60) * 5f)
		drawMinuteHand(canvas, calendar[Calendar.MINUTE].toFloat())
		drawSecondsHand(canvas, calendar[Calendar.SECOND].toFloat())
	}
}