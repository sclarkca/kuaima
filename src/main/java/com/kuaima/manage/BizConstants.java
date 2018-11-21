package com.kuaima.manage;

public interface BizConstants {

	/**
	 * 生效状态
	 */
	public interface ACTIVE {

		/**
		 * 状态
		 */
		public interface STATUS {
			// 生效
			public static final String ACTIVE = "1";
			// 关
			public static final String INACTIVE = "2";
		}

	}

	/**
	 * 是否
	 */
	public interface YES_NO {

		public static final int 是 = 1;
		// 关
		public static final int 否 = 0;

		public static final String YES = "1";

		public static final String NO = "0";
	}

	/**
	 * 处理状态
	 */
	public interface PROCESS_STATUS {

		public static final String 待处理 = "1";
		// 关
		public static final String 处理中 = "2";

		public static final String 已解决 = "3";

		public static final String 已标记 = "4";
	}

}
