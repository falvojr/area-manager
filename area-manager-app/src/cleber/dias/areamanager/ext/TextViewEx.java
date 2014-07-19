package cleber.dias.areamanager.ext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Requires {@link TextJustifyUtils}!<br>
 * <br>
 * 
 * From TextJustify-Android Library v1.0.2<br>
 * https://github.com/bluejamesbond/TextJustify-Android<br>
 * <br>
 * 
 * Please report any issues<br>
 * https://github.com/bluejamesbond/TextJustify-Android/issues<br>
 * <br>
 * 
 * Date: 12/13/2013 12:28:16 PM<br>
 * 
 * @author Mathew Kurian
 */
public class TextViewEx extends TextView {
	private Paint paint = new Paint();

	private String[] blocks;
	private float spaceOffset = 0;
	private float horizontalOffset = 0;
	private float verticalOffset = 0;
	private float horizontalFontOffset = 0;
	private float dirtyRegionWidth = 0;
	private boolean wrapEnabled = false;
	int left, top, right, bottom = 0;
	private Align _align = Align.LEFT;
	private float strecthOffset;
	private float wrappedEdgeSpace;
	private String block;
	private String wrappedLine;
	private String[] lineAsWords;
	private Object[] wrappedObj;

	private Bitmap cache = null;
	private boolean cacheEnabled = false;

	public TextViewEx(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// set a minimum of left and right padding so that the texts are not too close to the side screen
		this.setPadding(10, 0, 10, 0);
	}

	public TextViewEx(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setPadding(10, 0, 10, 0);
	}

	public TextViewEx(Context context) {
		super(context);
		this.setPadding(10, 0, 10, 0);
	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		super.setPadding(left + 10, top, right + 10, bottom);
	}

	@Override
	public void setDrawingCacheEnabled(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
	}

	public void setText(String st, boolean wrap) {
		this.wrapEnabled = wrap;
		super.setText(st);
	}

	public void setTextAlign(Align align) {
		this._align = align;
	}

	@SuppressLint({ "NewApi", "DrawAllocation" })
	@Override
	protected void onDraw(Canvas canvas) {
		// If wrap is disabled then, request original onDraw
		if (!this.wrapEnabled) {
			super.onDraw(canvas);
			return;
		}

		// Active canas needs to be set based on cacheEnabled
		Canvas activeCanvas = null;

		// Set the active canvas based on whether cache is enabled
		if (this.cacheEnabled) {

			if (this.cache != null) {
				// Draw to the OS provided canvas if the cache is not empty
				canvas.drawBitmap(this.cache, 0, 0, this.paint);
				return;
			} else {
				// Create a bitmap and set the activeCanvas to the one derived from the bitmap
				this.cache = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Config.ARGB_4444);
				activeCanvas = new Canvas(this.cache);
			}
		} else {
			// Active canvas is the OS provided canvas
			activeCanvas = canvas;
		}

		// Pull widget properties
		this.paint.setColor(this.getCurrentTextColor());
		this.paint.setTypeface(this.getTypeface());
		this.paint.setTextSize(this.getTextSize());
		this.paint.setTextAlign(this._align);
		this.paint.setFlags(Paint.ANTI_ALIAS_FLAG);

		// Minus out the paddings pixel
		this.dirtyRegionWidth = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
		int maxLines = Integer.MAX_VALUE;
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			maxLines = this.getMaxLines();
		}
		int lines = 1;
		this.blocks = this.getText().toString().split("((?<=\n)|(?=\n))");
		this.verticalOffset = this.horizontalFontOffset = this.getLineHeight() - 0.5f;
		this.spaceOffset = this.paint.measureText(" ");

		for (int i = 0; (i < this.blocks.length) && (lines <= maxLines); i++) {
			this.block = this.blocks[i];
			this.horizontalOffset = 0;

			if (this.block.length() == 0) {
				continue;
			} else if (this.block.equals("\n")) {
				this.verticalOffset += this.horizontalFontOffset;
				continue;
			}

			this.block = this.block.trim();

			if (this.block.length() == 0) {
				continue;
			}

			this.wrappedObj = TextJustifyUtils.createWrappedLine(this.block, this.paint, this.spaceOffset, this.dirtyRegionWidth);

			this.wrappedLine = ((String) this.wrappedObj[0]);
			this.wrappedEdgeSpace = (Float) this.wrappedObj[1];
			this.lineAsWords = this.wrappedLine.split(" ");
			this.strecthOffset = this.wrappedEdgeSpace != Float.MIN_VALUE ? this.wrappedEdgeSpace / (this.lineAsWords.length - 1) : 0;

			for (int j = 0; j < this.lineAsWords.length; j++) {
				String word = this.lineAsWords[j];
				if ((lines == maxLines) && (j == (this.lineAsWords.length - 1))) {
					activeCanvas.drawText("...", this.horizontalOffset, this.verticalOffset, this.paint);

				} else if (j == 0) {
					// if it is the first word of the line, text will be drawn starting from right edge of textview
					if (this._align == Align.RIGHT) {
						activeCanvas.drawText(word, this.getWidth() - (this.getPaddingRight()), this.verticalOffset, this.paint);
						// add in the paddings to the horizontalOffset
						this.horizontalOffset += this.getWidth() - (this.getPaddingRight());
					} else {
						activeCanvas.drawText(word, this.getPaddingLeft(), this.verticalOffset, this.paint);
						this.horizontalOffset += this.getPaddingLeft();
					}

				} else {
					activeCanvas.drawText(word, this.horizontalOffset, this.verticalOffset, this.paint);
				}
				if (this._align == Align.RIGHT) {
					this.horizontalOffset -= this.paint.measureText(word) + this.spaceOffset + this.strecthOffset;
				} else {
					this.horizontalOffset += this.paint.measureText(word) + this.spaceOffset + this.strecthOffset;
				}
			}

			lines++;

			if (this.blocks[i].length() > 0) {
				this.blocks[i] = this.blocks[i].substring(this.wrappedLine.length());
				this.verticalOffset += this.blocks[i].length() > 0 ? this.horizontalFontOffset : 0;
				i--;
			}
		}

		if (this.cacheEnabled) {
			// Draw the cache onto the OS provided canvas.
			canvas.drawBitmap(this.cache, 0, 0, this.paint);
		}
	}
}
