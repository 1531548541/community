package com.atguigu.community.dto;

import lombok.Data;

import java.util.HashMap;

/**
 * 全局统一返回结果
 */
@Data
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = -8157613083634272196L;

	public static final String CODE_TAG = "retcode";
	public static final String MSG_TAG = "retinfo";
	public static final String DATA_TAG = "data";

	/**
	 * 返回状态
	 */
	public enum ResponseCode {

		SUCCESS("000000", "成功"),
		ERROR("000001", "请求失败，请稍后再试"),
		USER_NOT_EXIST("000002", "用户不存在或停用");

		private final String code;
		private final String msg;

		ResponseCode(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		public String getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}
	}

	/**
	 * 状态类型
	 */
	private ResponseCode responseCode;
	/**
	 * 数据对象
	 */
	private Object data;

	public R() {
	}

	public R(ResponseCode responseCode) {
		super.put(CODE_TAG, responseCode.getCode());
		super.put(MSG_TAG, responseCode.getMsg());
	}

	public R(ResponseCode responseCode, String key, Object data) {
		super.put(CODE_TAG, responseCode.getCode());
		super.put(MSG_TAG, responseCode.getMsg());
		super.put(key, data);
	}

	public R(ResponseCode responseCode, String msg) {
		super.put(CODE_TAG, responseCode.getCode());
		super.put(MSG_TAG, msg);
	}

	public static R ok() {
		return new R(ResponseCode.SUCCESS);
	}

	public static R ok(Object data) {
		return new R(ResponseCode.SUCCESS, DATA_TAG, data);
	}

	public static R ok(String key, Object data) {
		return new R(ResponseCode.SUCCESS, key, data);
	}

	public static R error() {
		return new R(ResponseCode.ERROR);
	}

	public static R error(String msg) {
		return new R(ResponseCode.ERROR, msg);
	}

	public static R error(ResponseCode code, String msg) {
		return new R(code, msg);
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
