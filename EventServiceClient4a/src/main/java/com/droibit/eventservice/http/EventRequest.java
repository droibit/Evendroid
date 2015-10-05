/**
 * 
 */
package com.droibit.eventservice.http;

import static com.android.volley.toolbox.HttpHeaderParser.parseCacheHeaders;
import static com.android.volley.toolbox.HttpHeaderParser.parseCharset;

import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.JsonSyntaxException;

/**
 * イベント支援サービスのAPIを使用するためのリクエスト
 *
 * @author kumagai
 *
 */
public class EventRequest<T extends IMappable> extends Request<T> {
	
	/** GETリクエスト情報 */
	private final IGetRequest mGetRequest;
	
	/** レスポンスをコールバックする */
	private final ResponseCallback<T> mCallback;
	
	/**
	 * 新しいインスタンスを作成する
	 * 
	 * @param getRequest GETリクエスト情報
	 * @param callback レスポンスのコールバック
	 */
    public EventRequest(IGetRequest getRequest, ResponseCallback<T> callback) {
        super(Method.GET, getRequest.getUri().toString(), callback);
        
        mGetRequest = getRequest;
        mCallback = callback;
    }


    public IGetRequest getInnerRequest() {
        return mGetRequest;
    }

	/** {@inheritDoc} */
	@Override
	protected void deliverResponse(T response) {
		this.mCallback.onResponse(response);
	}

	/** {@inheritDoc} */
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            final String contents = new String(
                    response.data, parseCharset(response.headers));
            final IResponseParser<T> parser = ResponseParser.from(mGetRequest.getResponseFormat());

            return Response.success(
                    parser.parse(mCallback.getClazz(), contents), parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
	}
}
