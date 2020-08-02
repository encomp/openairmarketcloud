package com.toolinc.openairmarket.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.toolinc.openairmarket.OpenAirMarketApplication;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.persistence.cloud.SaleRepository;

import java.math.BigDecimal;

import javax.inject.Inject;

import timber.log.Timber;

/** Implementation of Sale Widget functionality. */
public class SaleWidget extends AppWidgetProvider {

  private static final String TAG = SaleWidget.class.getSimpleName();
  @Inject SaleRepository saleRepository;

  static void updateAppWidget(
      Context context,
      AppWidgetManager appWidgetManager,
      int appWidgetId,
      SaleRepository saleRepository) {
    // Construct the RemoteViews object
    new SaleAmount(context, appWidgetManager, appWidgetId, saleRepository).updateSale();
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(
        appWidgetId, new RemoteViews(context.getPackageName(), R.layout.sale_widget));
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    inject(context);
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId, saleRepository);
    }
  }

  private void inject(Context context) {
//    ((OpenAirMarketApplication) context.getApplicationContext())
//        .getOpenAirMarketInjector()
//        .inject(this);
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }

  /** Performs the update of the sale amount. */
  private static class SaleAmount implements OnSuccessListener<BigDecimal>, OnFailureListener {

    private Context context;
    private AppWidgetManager appWidgetManager;
    private int appWidgetId;
    private SaleRepository saleRepository;

    public SaleAmount(
        Context context,
        AppWidgetManager appWidgetManager,
        int appWidgetId,
        SaleRepository saleRepository) {
      this.context = context;
      this.appWidgetManager = appWidgetManager;
      this.appWidgetId = appWidgetId;
      this.saleRepository = saleRepository;
    }

    public void updateSale() {
      Timber.tag(TAG).d("About to retrieve the sale amount.");
      saleRepository.retrieveSalesTotal(24, this, this);
    }

    @Override
    public void onSuccess(BigDecimal bigDecimal) {
      Timber.tag(TAG).d("Sale amount $[%s].", bigDecimal.toPlainString());
      RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.sale_widget);
      remoteViews.setTextViewText(
          R.id.tv_sale_amount, OpenAirMarketApplication.toString(bigDecimal));
      appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onFailure(@NonNull Exception e) {
      Timber.tag(TAG).d("Sale amount failed [%s].", e.getMessage());
    }
  }
}
