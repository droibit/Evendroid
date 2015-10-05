package com.droibit.eventservice.test;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.droibit.eventservice.EventServiceClient;
import com.droibit.eventservice.IEventServiceClient;
import com.droibit.eventservice.events.EventServices;
import com.droibit.eventservice.http.GetRequest;
import com.droibit.eventservice.http.IGetRequest;
import com.droibit.eventservice.http.url.EventParameters;
import com.droibit.eventservice.http.url.RequestContents;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class LibraryTest extends ApplicationTestCase<Application> {

    public LibraryTest() {
        super(Application.class);
    }

    public void loadTest() {

        GetRequest.Builder builder = new GetRequest.Builder()
                .append(EventServices.ATND, RequestContents.EVENT)
                .append(EventParameters.KEYWORD, "android")
                .append(EventParameters.COUNT, "10");
        IGetRequest getRequest = builder.build();

        GetRequest.UrlHostPath hostPath = RequestContents.EVENT.getHostPath(EventServices.ATND);
        assertEquals(getRequest.getUri().getHost(), hostPath.getHost());
        assertEquals(getRequest.getUri().getPath(), hostPath.getPath());
        assertEquals(getRequest.getParameter().get(EventParameters.KEYWORD), "android");
        assertEquals(getRequest.getParameter().get(EventParameters.COUNT), "10");
        assertEquals(getRequest.getParameter().get(EventParameters.FORMAT), "json");


//        IEventServiceClient client = new EventServiceClient(getContext());
//        IGetRequest getRequest = new GetRequest.Builder()
//                .append(EventServices.ATND, RequestContents.EVENT)


    }
}