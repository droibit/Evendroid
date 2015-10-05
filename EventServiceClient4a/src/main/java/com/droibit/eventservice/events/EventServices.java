package com.droibit.eventservice.events;


/**
 * イベントの種類の列挙体。<br>
 * 現在はeventATND, ATND BETA, Zusaar, Connpassの4種類。
 *
 * @author kumagai
 *
 */
public enum EventServices {
	ATND("ATND", 1),
	ZUSAAR("Zusaar", 2),
	CONNPASS("connpass", 3);
	
	/** イベント支援サービス名 */
	private final String mName;

    /** サービスの識別ID */
    private final int mId;
	
	/***
	 * プライベートコンストラクタ
	 * 
	 * @param name サービス名
     * @param id 識別ID
 	 */
	EventServices(String name, int id) {
		this.mName = name;
        this.mId = id;
	}
	
	/**
	 * イベント支援サービス名を取得する
	 * 
	 * @return イベント支援サービス名
	 */
	public final String getName() {
		return mName;
	}

    /**
     * イベント支援サービスの識別IDを取得する
     *
     * @return イベント支援サービスの識別ID
     */
    public final int getId() { return mId; }
    	
	/**
	 * サービス名からサービスの種類を取得する
	 * 
	 * @param name サービス名
	 * @return サービスの種類
	 */
	public static EventServices from(String name) {
		for (EventServices type : values()) {
			if (type.mName.equals(type)) {
				return type;
			}
		}
		throw new IllegalArgumentException();
	}

    /**
     * サービスの識別IDからサービスの種類を取得する
     *
     * @param id サービスの識別ID
     * @return サービスの種類
     */
    public static EventServices from(int id) {
        for (EventServices type : values()) {
            if (type.mId == id) {
                return type;
            }
        }
        throw new IllegalArgumentException();
    }
}
