package com.example.sensorapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.concurrent.TimeUnit

class GraphView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var drawPaint: Paint = Paint()
    private var mPoints : ArrayDeque<Point> = ArrayDeque()
    private var mPaint : Paint = Paint()

    init {
        setupPaint()
    }

    private fun setupPaint() {
        drawPaint = Paint()
        drawPaint.setColor(Color.BLUE)
        drawPaint.setAntiAlias(true)
        drawPaint.setStrokeWidth(3F)
        drawPaint.setStyle(Paint.Style.FILL_AND_STROKE)
        drawPaint.setStrokeJoin(Paint.Join.ROUND)
        drawPaint.setStrokeCap(Paint.Cap.ROUND)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(0f, canvas.height/2.toFloat(), canvas.width.toFloat(), canvas.height/2.toFloat(), mPaint)
        canvas.drawLine(1f, 0f, 1f, canvas.height.toFloat(), mPaint)
        if (mPoints.size > 0) {
            var oldPoint: Point = Point(0,0)
            for(point in mPoints) {
                  canvas.drawLine(oldPoint.x.toFloat(),
                      canvas.height/2.toFloat()-oldPoint.y.toFloat()/100*canvas.height.toFloat(),
                      point.x.toFloat(),
                      canvas.height/2.toFloat()-point.y.toFloat()/100*canvas.height.toFloat(), mPaint)
                  oldPoint = point
            }
        }
    }

     fun drawChart( points : ArrayList<Point>, backgroundColor : Int, foregroundColor : Int) {
         mPoints.addAll(points);
         mPaint = Paint();
         mPaint.setColor(foregroundColor);
         mPaint.setStrokeWidth(3f);
         mPaint.setStyle(Paint.Style.STROKE);
         invalidate()
    }

    fun clear()
    {
        mPoints = ArrayDeque()
        invalidate()
    }
}