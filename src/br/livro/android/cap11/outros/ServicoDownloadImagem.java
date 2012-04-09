package br.livro.android.cap11.outros;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Serviço que faz o download de uma imagem e cria uma notificação
 * 
 * @author ricardo
 * 
 */
public class ServicoDownloadImagem extends Service {
	private final String CATEGORIA = "livro";
	private static final String URL = "http://code.google.com/intl/pt-BR/android/images/google-apis.png";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(CATEGORIA, "ServicoDownloadImagem > onCreate()");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.i(CATEGORIA, "ServicoDownloadImagem > onStart()");

		downloadImagem(URL);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(CATEGORIA, "ServicoDownloadImagem > onDestroy()");
	}

	private void downloadImagem(final String urlImg) {
		new Thread() {
			@Override
			public void run() {

				try {
					Log.i(CATEGORIA, "ServicoDownloadImagem > Buscando imagem...");

					// Cria a URL
					URL u = new URL(URL);

					HttpURLConnection connection = (HttpURLConnection) u.openConnection();
					// Configura a requisição como "get"
					connection.setRequestProperty("Request-Method", "GET");
					connection.setDoInput(true);
					connection.setDoOutput(false);

					connection.connect();

					InputStream in = connection.getInputStream();

					Log.i(CATEGORIA, "ServicoDownloadImagem > Lendo imagem...");

					// String arquivo = readBufferedString(sb, in);
					byte[] bytesImagem = readBytes(in);

					Log.i(CATEGORIA, "ServicoDownloadImagem > Imagem lida com sucesso!");

					connection.disconnect();

					Log.i(CATEGORIA, "ServicoDownloadImagem > Criando notificação...");

					criarNotificacao(bytesImagem);

					Log.i(CATEGORIA, "ServicoDownloadImagem > Notificação criada com sucesso.");

					stopSelf();

				} catch (IOException e) {
					Log.e(CATEGORIA, e.getMessage(), e);
				}
			}
		}.start();
	}

	private byte[] readBytes(InputStream in) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			byte[] bytes = bos.toByteArray();
			return bytes;
		} finally {
			bos.close();
			in.close();
		}
	}

	// Exibe a notificação
	protected void criarNotificacao(byte[] bytesImagem) {

		String mensagemBarraStatus = "Fim do download.";
		String titulo = "Download completo.";
		String mensagem = "Visualizar imagem do download.";
		Class<?> activity = VisualizarImagem.class;

		// Service
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Notification notificacao = new Notification(R.drawable.smile, mensagemBarraStatus, System.currentTimeMillis());

		// PendingIntent para executar a Activity se o usuário selecionar a notificação
		Intent intentMensagem = new Intent(this, activity);
		intentMensagem.putExtra("imagem", bytesImagem);
		PendingIntent p = PendingIntent.getActivity(this, 0, intentMensagem, 0);

		// Informações
		notificacao.setLatestEventInfo(this, titulo, mensagem, p);

		// Precisa de permissão: <uses-permission android:name="android.permission.VIBRATE" />
		// espera 100ms e vibra por 250ms, depois espera por 100 ms e vibra por 500ms.
		notificacao.vibrate = new long[] { 100, 250, 100, 500 };

		// id (número único) que identifica esta notificação. Mesmo id utilizado para cancelar
		nm.notify(R.string.app_name, notificacao);
	}
}