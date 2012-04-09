package br.livro.android.cap11.outros;

import android.app.Activity;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

/**
 * Tela que apenas exibe uma imagem
 * 
 * @author ricardo
 * 
 */
public class VisualizarImagem extends Activity {
	private static final String CATEGORIA = "livro";

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Log.i(CATEGORIA, "VisualizarImagem > onCreate()");

		ImageView imgView = new ImageView(this);
		setContentView(imgView);

		byte[] bytesImagem = getIntent().getExtras().getByteArray("imagem");

		Log.i(CATEGORIA, "VisualizarImagem > Criando a imagem");
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytesImagem, 0, bytesImagem.length);

		Log.i(CATEGORIA, "Imagem: " + imgView);
		imgView.setImageBitmap(bitmap);

		Log.i(CATEGORIA, "VisualizarImagem > Cancelando a notificação...");
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(R.string.app_name);

		Log.i(CATEGORIA, "VisualizarImagem > Fim.");
	}
}