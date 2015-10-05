/**
 * 
 */
package com.droibit.text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.text.format.DateFormat;

/**
 * 
 * 
 * @author kumagaishinya
 * 
 */
public class ChangeLogHTML {
	private String style = "h1 { margin-left: 0px; font-size: 12pt; }"
			+ "li { margin-left: 0px; font-size: 9pt; }"
			+ "ul { padding-left: 30px; }"
			+ ".summary { font-size: 9pt; color: #606060; display: block; clear: left; }"
			+ ".date { font-size: 9pt; color: #606060;  display: block; }";

	private Context context;
	private int resChangeLog;

	public ChangeLogHTML(Context context, int resChangeLog) {
		this.context = context;
		this.resChangeLog = resChangeLog;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		// Get resources
		final String packageName = context.getPackageName();
		final Resources resources;
		try {
			resources = context.getPackageManager().getResourcesForApplication(
					packageName);
		} catch (NameNotFoundException ignored) {
			return Strings.EMPTY;
		}

		// Create HTML change log
		return getHtmlChangeLog(resources, 0);
	}

	public String getHtmlChangeLog(Resources resources,
			int version) {
		boolean releaseFound = false;
		final StringBuilder changelogBuilder = new StringBuilder();
		changelogBuilder.append("<html><head>").append(getStyle())
				.append("</head><body>");
		final XmlResourceParser xml = resources.getXml(resChangeLog);
		try {
			int eventType = xml.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if ((eventType == XmlPullParser.START_TAG)
						&& (xml.getName().equals("release"))) {
					// Check if the version matches the release tabName.
					// When version is 0 every release tabName is parsed.
					final int versioncode = Integer.parseInt(xml
							.getAttributeValue(null, "versioncode"));
					if ((version == 0) || (versioncode == version)) {
						parseReleaseTag(changelogBuilder, xml);
						// At lease one release tabName has been parsed.
						releaseFound = true;
					}
				}
				eventType = xml.next();
			}
		} catch (XmlPullParserException e) {
			// Log.e(TAG, e.getMessage(), e);
			return "";
		} catch (IOException e) {
			// Log.e(TAG, e.getMessage(), e);
			return "";
		} finally {
			xml.close();
		}
		changelogBuilder.append("</body></html>");

		// Check if there was a release tabName parsed, if not return an empty
		// string.
		if (releaseFound) {
			return changelogBuilder.toString();
		} else {
			return "";
		}
	}

	private void parseReleaseTag(final StringBuilder changelogBuilder,
			final XmlPullParser resourceParser) throws XmlPullParserException,
			IOException {
		changelogBuilder.append("<h1>Release: ")
				.append(resourceParser.getAttributeValue(null, "version"))
				.append("</h1>");

		// Add date if available
		if (resourceParser.getAttributeValue(null, "date") != null) {
			changelogBuilder
					.append("<span class='date'>")
					.append(parseDate(resourceParser.getAttributeValue(null,
							"date"))).append("</span>");
		}

		// Add summary if available
		if (resourceParser.getAttributeValue(null, "summary") != null) {
			changelogBuilder.append("<span class='summary'>")
					.append(resourceParser.getAttributeValue(null, "summary"))
					.append("</span>");
		}

		changelogBuilder.append("<ul>");

		// Parse child nodes
		int eventType = resourceParser.getEventType();
		while ((eventType != XmlPullParser.END_TAG)
				|| (resourceParser.getName().equals("change"))) {
			if ((eventType == XmlPullParser.START_TAG)
					&& (resourceParser.getName().equals("change"))) {
				eventType = resourceParser.next();
				changelogBuilder.append("<li>" + resourceParser.getText()
						+ "</li>");
			}
			eventType = resourceParser.next();
		}
		changelogBuilder.append("</ul>");
	}

	@SuppressLint("SimpleDateFormat")
	private String parseDate(final String dateString) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		try {
			final Date parsedDate = dateFormat.parse(dateString);
			return DateFormat.getDateFormat(context).format(parsedDate);
		} catch (ParseException ignored) {
			// If there is a problem parsing the date just return the original
			// string
			return dateString;
		}
	}

	public String getStyle() {
		return String.format("<style type=\"text/css\">%s</style>", style);
	}

	public void setStyle(String style) {
		this.style = style;
	}
}
