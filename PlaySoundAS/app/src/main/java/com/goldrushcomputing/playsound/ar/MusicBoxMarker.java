package com.goldrushcomputing.playsound.ar;

import com.goldrushcomputing.playsound.Example;
import com.goldrushcomputing.playsound.geom.Matrix4f;
import com.goldrushcomputing.playsound.geom.Vector4f;
import org.artoolkit.ar.base.ARToolKit;

public class MusicBoxMarker extends Marker {
	private Matrix4f markerMat;

	private Matrix4f workMat = new Matrix4f();
	private Vector4f workVec0 = new Vector4f();
	private Vector4f workVec1 = new Vector4f();

	/// 最後にトラックされた位置が画面の上半分か下半分か
	private int lastTrackSide = 0;

	/**
	 * 上下のどちらにあるかどうかをチェック.
	 */
	private int calcTrackingSide(Matrix4f projMat) {
		if( !isTracked() ) {
			return 0;
		}

		float markerMatrix[] = ARToolKit.getInstance().queryMarkerTransformation(markerId);
		if (markerMatrix == null) {
			return 0;
		}

		if (markerMat == null) {
			markerMat = new Matrix4f();
		}

		markerMat.set(markerMatrix);

		// プロジェクション行列とマーカーのModelView行列をかけて、3D座標からViewPort座標へ変換する行列を作成する
		workMat.set(projMat);
		workMat.mul(markerMat);

		workVec0.set(0.0f, 0.0f, 0.0f, 1.0f);
		workMat.transform(workVec0, workVec1);

		// ViewPort座標系でのX座標値を得る
		// 縦横が反転しているので、縦方向がX軸
		float sx = workVec1.x / workVec1.w;

		if ( sx < 0.0f ) {
			// 画面の下半分
			return -1;
		} else {
			// 画面の上半分
			return 1;
		}
	}

	void checkPlaySoundOverLine(long now, Example activity, Matrix4f projMat) {
		int side = calcTrackingSide(projMat);
		if( side != 0 && lastTrackSide != 0 && side != lastTrackSide ) {
			if( now - lastPlayTime > 100 ) {
				activity.playSound(soundId);
				lastPlayTime = now;
			}
		}
		lastTrackSide = side;
	}
}