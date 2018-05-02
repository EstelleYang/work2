package lzjs.com.picplayer.bean;

import android.view.View;

/**
 * 描述：广告信息</br>
 */
public class ADInfo implements Comparable<ADInfo>{
	
	String id = "";
	String url = "";
	String content = "";
	String type = "";
	private onGridViewItemClickListenerSpecial onClickListener;

	public onGridViewItemClickListenerSpecial getOnClickListener() {
		return onClickListener;
	}
	public void setOnClickListener(onGridViewItemClickListenerSpecial onClickListener) {
		this.onClickListener = onClickListener;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Compares this object to the specified object to determine their relative
	 * order.
	 *
	 * @param another the object to compare to this instance.
	 * @return a negative integer if this instance is less than {@code another};
	 * a positive integer if this instance is greater than
	 * {@code another}; 0 if this instance has the same order as
	 * {@code another}.
	 * @throws ClassCastException if {@code another} cannot be converted into something
	 *                            comparable to {@code this} instance.
	 */
	@Override
	public int compareTo(ADInfo another) {
		return 0;
	}

	public interface onGridViewItemClickListenerSpecial
	{
		void ongvItemClickListenerSpecial(View v);
	}

}
