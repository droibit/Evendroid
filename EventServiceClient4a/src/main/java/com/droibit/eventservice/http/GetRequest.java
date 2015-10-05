/**
 * 
 */
package com.droibit.eventservice.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Observable;
import java.util.Observer;
import java.util.EventObject;

import android.net.Uri;

import com.droibit.eventservice.events.EventServices;
import com.droibit.eventservice.http.url.EventParameters;
import com.droibit.eventservice.http.url.IParameterKey;
import com.droibit.eventservice.http.url.RequestContents;
import com.droibit.eventservice.http.url.ResponseFormats;
import com.droibit.eventservice.http.url.UrlParameter;

/**
 * イベント支援サービスへ送信するHTTPリクエスト。<br>
 * リクエスト作成後、{@code UrlParameter}を変更した場合は自動的に
 * {@code Uri}が再生成される。
 * 
 * @author kumagai
 * 
 */
public class GetRequest implements IGetRequest {

    private static final String ENCODE_UTF8 = "UTF-8";

	/**
	 * イベント支援サービスのURL(ホスト+パス）
	 * 
	 * @author kumagai
	 * 
	 */
	public static final class UrlHostPath {

		/** URLのスキーマ部 */
		private static final String SCHEME = "http";

		/** URLのホスト部 */
		private final String mHost;

		/** URLのパス部 */
		private final String mPath;
		
		/**
		 * 新しいインスタンスを作成する
		 * 
		 * @param host ホスト名
		 * @param path URL部
		 */
		public UrlHostPath(String host, String path) {
			mHost = host;
			mPath = path;
		}

		/**
		 * URLのホストを取得する
		 * 
		 * @return URLのホスト部
		 */
		public String getHost() {
			return mHost;
		}

		/**
		 * URLのパスを取得する
		 * 
		 * @return URLのパス部
		 */
		public String getPath() {
			return mPath;
		}
	}

	/**
	 * ベント支援サービスのHTTPリクエスト作成するビルダ
	 * 
	 * @author kumagai
	 * 
	 */
	public static final class Builder implements IGetRequest.Builder {

		/** リクリエストのURL */
		private UrlHostPath mHostPath;

		/** URLパラメータ */
		private UrlParameter mParams;

		/**
		 * 新しいインスタンスを作成する
		 */
		public Builder() {
			mParams = new UrlParameter();
		}

		/**
		 * リクエストのURLを追加する
		 * 
		 * @param serviceType イベント支援サービスの種類
         * @param contentType リクエストの種類
		 * @return 自身の参照
		 */
		public Builder append(EventServices serviceType, RequestContents contentType) {
			this.mHostPath = contentType.getHostPath(serviceType);
			return this;
		}

		/**
		 * URLパラメータを追加する
		 * 
		 * @param key URLパラメータのキー値
		 * @param value URLパラメータ値
		 * @return 自身の参照
		 */
		public Builder append(IParameterKey key, String value) {
            try {
                mParams.put(key, URLEncoder.encode(value, GetRequest.ENCODE_UTF8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return this;
		}

		/**
		 * {@link IGetRequest}を作成する
		 * 
		 * @return {@link IGetRequest}オブジェクト
		 */
		public IGetRequest build() {
			final Uri.Builder builder = new Uri.Builder().scheme(UrlHostPath.SCHEME)
					.authority(mHostPath.mHost).path(mHostPath.mPath);
			
			if (!mParams.isEmpty()) {
				builder.encodedQuery(mParams.toString());
			}
			return new GetRequest(builder.build(), mParams);
		}
	}
	
	/**
	 * URLパラメータが変更された時に呼ばれるイベントリスナ
	 *
	 * @author kumagai
	 *
	 */
	public class ChangedParameterObserver implements Observer {

		/** {@inheritDoc} */
		@Override
		public void update(Observable observable, Object data) {
			final EventObject event = (EventObject) data;
			final UrlParameter changedParams = (UrlParameter) event.getSource();
			
			// URLを再生成する
			final Uri.Builder builder = mUri.buildUpon();
			// URLパラメータがクリアされた場合
			if (changedParams.isEmpty()) {
				builder.clearQuery();
			} else {
				// 追加もしくは変更された場合
				builder.query(changedParams.toString());
			}
			mUri = builder.build();
		}
	}
	
	/** リクエストURL */
	private Uri mUri;
	
	/** リクエストのURLパラメータ */
	private final UrlParameter mParams;
	
	/** レスポンス形式 */
	private final ResponseFormats mResponseFormat;
	
	/**
	 * 新しいコミットを作成する
	 * 
	 * @param uri リクエストURL
	 * @param params リクエストのURLパラメータ
	 */
	private GetRequest(Uri uri, UrlParameter params) {
		mUri = uri;
		mParams = params;
		
		// URLパラメータが変更されたらURLを作りなおす
		params.addObserver(new ChangedParameterObserver());
		
		// レスポンス形式を取得する
		final String format = params.get(EventParameters.FORMAT);
		if (format != null) {
			this.mResponseFormat = ResponseFormats.from(format);
		} else {
			throw new IllegalStateException("URLパラメータにレスポンスのフォーマットが存在しません");
		}
	}

	/** {@inheritDoc} */
	@Override
	public Uri getUri() {
		return mUri;
	}

	/** {@inheritDoc} */
	@Override
	public UrlParameter getParameter() {
		return mParams;
	}

	/** {@inheritDoc} */
	@Override
	public ResponseFormats getResponseFormat() {
		return mResponseFormat;
	}
}
