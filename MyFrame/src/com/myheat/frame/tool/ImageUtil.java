package com.myheat.frame.tool;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;

/**
 * 图片处理工具类
 * 
 * @author myheat
 * 
 */
public class ImageUtil {

	/**
	 * Bitmap → byte[]
	 * 
	 * @param bm
	 * @return
	 */
	public byte[] BitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * byte[] → Bitmap
	 * 
	 * @param b
	 * @return
	 */
	public Bitmap BytesToBimap(byte[] b) {
		if (b != null && b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * 等比例放大缩小图片
	 * 
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		float scaleWidth = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight);
		// matrix.postScale(Float.valueOf("0.20454545"),
		// Float.valueOf("0.20769231"));
		Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newBmp;
	}

	/**
	 * Bitmap转换成Drawable
	 * 
	 * @param act
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawable(Activity act, Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(act.getResources(), bitmap);

		return bd;
	}

	/**
	 * 将Drawable转化为Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Drawable缩放
	 * 
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		// drawable转换成bitmap
		Bitmap oldbmp = drawableToBitmap(drawable);
		// 创建操作图片用的Matrix对象
		Matrix matrix = new Matrix();
		// 计算缩放比例
		float sx = ((float) w / width);
		float sy = ((float) h / height);
		// 设置缩放比例
		matrix.postScale(sx, sy);
		// 建立新的bitmap，其内容是对原bitmap的缩放后的图
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true);
		return new BitmapDrawable(newbmp);
	}

	/**
	 * 获得圆角图片
	 * 
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	/**
	 * 获得带倒影的图片
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
				h / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
				Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * 处理图片OOM
	 * 
	 * @param context
	 * @param path
	 * @param size
	 * @return
	 * @throws IOException
	 */
	public static Bitmap revitionImageSize(Context context, String path,
			int size) throws IOException {
		// 取得图片
		InputStream temp;
		File file = new File(path);
		file.createNewFile();
		temp = new FileInputStream(file);
		temp.close();
		// InputStream temp = context.getAssets().open(path);
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
		options.inJustDecodeBounds = true;
		// 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
		BitmapFactory.decodeStream(temp, null, options);
		// 关闭流
		temp.close();

		// 生成压缩的图片
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			// 这一步是根据要设置的大小，使宽和高都能满足
			if ((options.outWidth >> i <= size)
					&& (options.outHeight >> i <= size)) {
				// 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
				temp = new FileInputStream(file);
				;
				// 这个参数表示 新生成的图片为原始图片的几分之一。
				options.inSampleSize = (int) Math.pow(2.0D, i);
				// 这里之前设置为了true，所以要改为false，否则就创建不出图片
				options.inJustDecodeBounds = false;

				bitmap = BitmapFactory.decodeStream(temp, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	/**
	 * 处理图片OOM
	 * 
	 * @param mContent
	 * @return
	 */
	public static Bitmap b(byte[] mContent) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		Bitmap bitmap = BitmapFactory.decodeByteArray(mContent, 0,
				mContent.length, options); // 此时返回bm为空
		options.inJustDecodeBounds = false;
		// 计算缩放比
		float be = (options.outWidth / (float) 80);
		if (be < 1.4)
			be = 1;
		else
			be = (float) (Math.floor(be) + 1);
		options.inSampleSize = (int) be;
		// 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
		bitmap = BitmapFactory.decodeByteArray(mContent, 0, mContent.length,
				options);
		ByteArrayOutputStream outstm = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, outstm);
		mContent = outstm.toByteArray();
		return bitmap;
	}

	public static InputStream input;
	public static String type = "";
	/*
	 * public static String postImage(Bitmap bit){ return
	 * byteArrayToBase64(bitmapToByteArray(bit)); }
	 * 
	 * public static String postText(String text){ return
	 * byteArrayToBase64(textToByteArray(text)); }
	 */
	/**
	 * 图片转化为字节数组
	 * 
	 * @param Bitmap
	 *            bit
	 * @return byte[]
	 */
	public static byte[] bitmapToByteArray(Bitmap bit) {
		byte data[] = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bit.compress(Bitmap.CompressFormat.JPEG, 100, out);
			data = out.toByteArray();
			type = "jpg";
		} catch (Exception e) {
			/*
			 * ByteArrayOutputStream out = new ByteArrayOutputStream();
			 * bit.compress(Bitmap.CompressFormat.PNG, 100, out); data =
			 * out.toByteArray(); type = "png";
			 */
		}
		return data;
	}

	/**
	 * 字符串转化为字节数组
	 * 
	 * @param String
	 *            str
	 * @return String
	 */
	private static byte[] textToByteArray(String str) {
		byte data[] = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeUTF(str);
			dos.close();
			data = baos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 保存图片到本地
	 * 
	 * @param bm
	 *            保存的图片
	 * 
	 * @return 图片路径
	 */
	public static String saveToLocal(Bitmap bm, String path, String fileName) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return null;
		}
		FileOutputStream fileOutputStream = null;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filePath = path + fileName;
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}
		if (!f.exists()) {
			try {
				f.createNewFile();
				fileOutputStream = new FileOutputStream(filePath);
				if (fileName.contains(".png")) {
					bm.compress(Bitmap.CompressFormat.PNG, 100,
							fileOutputStream);
				} else if (fileName.contains(".jpg")) {
					bm.compress(Bitmap.CompressFormat.JPEG, 100,
							fileOutputStream);
				}
			} catch (IOException e) {
				return null;
			} finally {
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (IOException e) {
					return null;
				}
			}
		}
		return filePath;
	}

	/**
	 * 创建一个缩放的图片
	 * 
	 * @param path
	 *            图片地址
	 * @param w
	 *            图片宽度
	 * @param h
	 *            图片高度
	 * @return 缩放后的图片
	 */
	public static Bitmap createBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片的旋转角度置为0
	 * 
	 * @Title: setPictureDegreeZero
	 * @param path
	 * @return void
	 */
	public static void setPictureDegreeZero(String path) {
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			// 修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
			// 例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
			exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
			exifInterface.saveAttributes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 从Uri中取图片保存到SDCARD中
	 * @param ctx
	 * @param imguri
	 * @param path
	 */
	public static Bitmap savePictureLocalUri(Context ctx, Uri imguri,
			String path) {
		Bitmap bitmap = null;
		try {
			ContentResolver resolver = ctx.getContentResolver();
			byte[] mContent;
			mContent = readInputStream(resolver.openInputStream(Uri.parse(imguri
					.toString())));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// 获取这个图片的宽和高
			bitmap = BitmapFactory.decodeByteArray(mContent, 0,
					mContent.length, options); // 此时返回bm为空
			options.inJustDecodeBounds = false;
			// 计算缩放比
			float be = (options.outWidth / (float) 480);
			if (be < 1.4)
				be = 1;
			else
				be = (float) (Math.floor(be) + 1);
			options.inSampleSize = (int) be;
			// 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
			bitmap = BitmapFactory.decodeByteArray(mContent, 0,
					mContent.length, options);

			File file = new File(path);
			if (file.exists()) {
				file.delete();
			} else {
				file.createNewFile();
			}

			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(CompressFormat.PNG, 100, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 根据路径获得图片并压缩，返回bitmap用于显示
	 * @param filePath
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 计算图片的缩放值
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	/**
	 * 旋转图片
	 * 
	 * @param angle
	 *            转换角度发生
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingBitmap(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		;
		matrix.postRotate(angle);
		// matrix.postScale(0.5f, 0.5f);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 压缩得办法 有更好的 请建议
	 * @param bmp
	 * @param maxSize
	 * @return
	 */
	public static Bitmap imageZoom(Bitmap bmp, double maxSize) {
		// 图片允许最大空间 单位：KB
		// 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
		Bitmap bitMap = bmp;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		// 将字节换成KB
		double mid = b.length / 1024;
		// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
		while (mid > maxSize) {
			// 获取bitmap大小 是允许最大大小的多少倍
			double i = mid / maxSize;
			// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
			// （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
			bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
					bitMap.getHeight() / Math.sqrt(i));
			mid = bmpToByteArray(bitMap, false).length / 1024;
		}
		return bitMap;
	}

	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	/**
	 * 把bitmap转字节数组
	 * 
	 * @param bmp
	 * @param needRecycle
	 * @return
	 */
	public static byte[] bmpToByteArray(final Bitmap bmp, boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取网络图片
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static byte[] getHttpImage(String path) throws Exception {
		// 构造一个URL对象
		URL url = new URL(path);
		// 使用openConnection打开URL对象
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 使用Http协议，设置请求方式为GET
		conn.setRequestMethod("GET");
		// 设置链接超时异常，5s
		conn.setConnectTimeout(5 * 1000);
		// 通过输入流获取图片数据
		InputStream inStream = conn.getInputStream();
		// 返回图片的二进制数据
		return readInputStream(inStream);
	}

	/**
	 * 从输入流里面得到返回为二进制的数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return byte[] 二进制数据
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		// 构造一个ByteArrayOutputStream
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 设置一个缓冲区
		byte[] buffer = new byte[1024];
		int len = 0;
		// 判断输入流长度是否等于-1，即非空
		while ((len = inStream.read(buffer)) != -1) {
			// 把缓冲区的内容写入到输出流中，从0开始读取，长度为len
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		inStream.close();
		return outStream.toByteArray();
	}
}
