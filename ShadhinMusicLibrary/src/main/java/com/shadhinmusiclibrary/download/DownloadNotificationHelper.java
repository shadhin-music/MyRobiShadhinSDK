package com.shadhinmusiclibrary.download;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.core.R;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.scheduler.Requirements;
import com.shadhinmusiclibrary.library.player.utils.CacheRepository;
import java.util.List;
import java.util.Map;

/**
 * Helper for creating download notifications.
 */
public final class DownloadNotificationHelper {

    private static final @StringRes
    int NULL_STRING_ID = 0;
    public static final String CANCEL_ACTION = "com.gm.shadhin.p.intent.cancel";

    private final NotificationCompat.Builder notificationBuilder;

    private final Context context;
    private final Map<String, String> downloadTitleMap;
    private final CacheRepository cacheRepository;

    /**
     * @param context         A context.
     * @param channelId       The id of the notification channel to use.
     * @param cacheRepository
     */
    public DownloadNotificationHelper(Context context, String channelId, Map<String, String> downloadTitleMap, CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
        this.notificationBuilder =
                new NotificationCompat.Builder(context.getApplicationContext(), channelId);

        this.downloadTitleMap = downloadTitleMap;
        this.context = context;
        this.notificationBuilder
                .addAction(
                        com.shadhinmusiclibrary.R.drawable.my_bl_sdk_ic_close,
                        "Cancel",
                        cancelPendingIntent()
                );


    }


    /**
     * Returns a progress notification for the given downloads.
     *
     * @param context            A context.
     * @param smallIcon          A small icon for the notification.
     * @param contentIntent      An optional content intent to send when the notification is clicked.
     * @param message            An optional message to display on the notification.
     * @param downloads          The downloads.
     * @param notMetRequirements Any requirements for downloads that are not currently met.
     * @return The notification.
     */
    public Notification buildProgressNotification(
            Context context,
            @DrawableRes int smallIcon,
            @Nullable PendingIntent contentIntent,
            @Nullable String message,
            List<Download> downloads,
            @Requirements.RequirementFlags int notMetRequirements) {

        float totalPercentage = 0;
        int downloadTaskCount = 0;
        boolean allDownloadPercentagesUnknown = true;
        boolean haveDownloadedBytes = false;
        boolean haveDownloadingTasks = false;
        boolean haveQueuedTasks = false;
        boolean haveRemovingTasks = false;
        for (int i = 0; i < downloads.size(); i++) {
            Download download = downloads.get(i);
            switch (download.state) {
                case Download.STATE_REMOVING:
                    haveRemovingTasks = true;
                    break;
                case Download.STATE_QUEUED:
                    haveQueuedTasks = true;
                    break;
                case Download.STATE_RESTARTING:
                case Download.STATE_DOWNLOADING:
                    haveDownloadingTasks = true;
                    float downloadPercentage = download.getPercentDownloaded();
                    if (downloadPercentage != C.PERCENTAGE_UNSET) {
                        allDownloadPercentagesUnknown = false;
                        totalPercentage += downloadPercentage;
                    }
                    haveDownloadedBytes |= download.getBytesDownloaded() > 0;
                    downloadTaskCount++;
                    break;
                // Terminal states aren't expected, but if we encounter them we do nothing.
                case Download.STATE_STOPPED:
                case Download.STATE_COMPLETED:
                case Download.STATE_FAILED:
                default:
                    break;
            }
        }

        String titleString = null;
        boolean showProgress = true;
        if (haveDownloadingTasks) {

            try {
                int size = downloads.size();
                String other = size > 1 ? " +" + (size - 1) + " songs" : "";
                titleString = downloadTitleMap.get(downloads.get(0).request.id) + other + " downloading";
            } catch (Exception ignored) {
            }


        } else if (haveQueuedTasks && notMetRequirements != 0) {
            showProgress = false;
            if ((notMetRequirements & Requirements.NETWORK_UNMETERED) != 0) {
                // Note: This assumes that "unmetered" == "WiFi", since it provides a clearer message that's
                // correct in the majority of cases.
                titleString = context.getResources().getString(R.string.exo_download_paused_for_wifi);
            } else if ((notMetRequirements & Requirements.NETWORK) != 0) {
                titleString = context.getResources().getString(R.string.exo_download_paused_for_network);
            } else {
                titleString = context.getResources().getString(R.string.exo_download_paused);
            }
        } else if (haveRemovingTasks) {
            titleString = context.getResources().getString(R.string.exo_download_removing);
        } else {
            // There are either no downloads, or all downloads are in terminal states.
            titleString = null;
        }

        int maxProgress = 0;
        int currentProgress = 0;
        boolean indeterminateProgress = false;
        if (showProgress) {
            maxProgress = 100;
            if (haveDownloadingTasks) {
                currentProgress = (int) (totalPercentage / downloadTaskCount);
                indeterminateProgress = allDownloadPercentagesUnknown && haveDownloadedBytes;
            } else {
                indeterminateProgress = true;
            }
        }

        return buildNotification(
                context,
                smallIcon,
                contentIntent,
                message,
                titleString,
                maxProgress,
                currentProgress,
                indeterminateProgress,
                /* ongoing= */ true,
                /* showWhen= */ false);
    }

    /**
     * Returns a notification for a completed download.
     *
     * @param context       A context.
     * @param smallIcon     A small icon for the notifications.
     * @param contentIntent An optional content intent to send when the notification is clicked.
     * @param message       An optional message to display on the notification.
     * @return The notification.
     */
    public Notification buildDownloadCompletedNotification(
            Context context,
            @DrawableRes int smallIcon,
            @Nullable PendingIntent contentIntent,
            @Nullable String message) {
        int titleStringId = R.string.exo_download_completed;
        return buildEndStateNotification(context, smallIcon, contentIntent, message, titleStringId);
    }

    /**
     * Returns a notification for a failed download.
     *
     * @param context       A context.
     * @param smallIcon     A small icon for the notifications.
     * @param contentIntent An optional content intent to send when the notification is clicked.
     * @param message       An optional message to display on the notification.
     * @return The notification.
     */
    public Notification buildDownloadFailedNotification(
            Context context,
            @DrawableRes int smallIcon,
            @Nullable PendingIntent contentIntent,
            @Nullable String message) {
        @StringRes int titleStringId = R.string.exo_download_failed;
        return buildEndStateNotification(context, smallIcon, contentIntent, message, titleStringId);
    }

    private Notification buildEndStateNotification(
            Context context,
            @DrawableRes int smallIcon,
            @Nullable PendingIntent contentIntent,
            @Nullable String message,
            @StringRes int titleStringId) {
        return buildNotification(
                context,
                smallIcon,
                contentIntent,
                message,
                (titleStringId == NULL_STRING_ID ? null : context.getResources().getString(titleStringId)),
                /* maxProgress= */ 0,
                /* currentProgress= */ 0,
                /* indeterminateProgress= */ false,
                /* ongoing= */ false,
                /* showWhen= */ true);
    }

    private Notification buildNotification(
            Context context,
            @DrawableRes int smallIcon,
            @Nullable PendingIntent contentIntent,
            @Nullable String message,
            String titleString,
            int maxProgress,
            int currentProgress,
            boolean indeterminateProgress,
            boolean ongoing,
            boolean showWhen) {
        notificationBuilder.setSmallIcon(smallIcon);
        notificationBuilder.setContentTitle(titleString);
        notificationBuilder.setContentIntent(contentIntent);
        notificationBuilder.setStyle(
                message == null ? null : new NotificationCompat.BigTextStyle().bigText(message));
        notificationBuilder.setProgress(maxProgress, currentProgress, indeterminateProgress);

        notificationBuilder.setOngoing(ongoing);
        notificationBuilder.setShowWhen(showWhen);
        return notificationBuilder.build();
    }

    private PendingIntent cancelPendingIntent() {
        Intent intent = new Intent(CANCEL_ACTION);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(context, 656, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context, 656, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        return pendingIntent;
    }

    private NotificationCompat.Action cancelAction() {
        Intent intent = new Intent(CANCEL_ACTION);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(context, 656, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context, 656, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        return new NotificationCompat.Action.Builder(
                com.shadhinmusiclibrary.R.drawable.my_bl_sdk_ic_close,
                "Cancel",
                pendingIntent)
                .build();


    }
}