/**
 * 
 */
package com.droibit.text;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;

/**
 * 
 * 
 * @author kumagaishinya
 * 
 */
public class OSSLicenseHTML {
	private String style = "h1 { margin-left: 0px; font-size: 12pt; }"
			+ "li { margin-left: 0px; font-size: 13spt; }"
			+ "ul { padding-left: 30px; }"
			+ ".license { padding: 12px; font-size: 10pt; color: #606060; display: block; clear: left; background-color: #CFCFCF;  }";

	private Context context;
	private int resLicense;

	public OSSLicenseHTML(Context context, int resLicense) {
		this.context = context;
		this.resLicense = resLicense;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		// TODO: Remove duplicate code with the method show()
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
		return getOSSLicenseHTML(resLicense, resources);
	}

	private String getOSSLicenseHTML(int resourceId, Resources resources) {
		boolean norticeFound = false;
		final StringBuilder changelogBuilder = new StringBuilder();
		changelogBuilder.append("<html><head>").append(getStyle())
				.append("</head><body>");
		final XmlResourceParser xml = resources.getXml(resourceId);
		try {
			int eventType = xml.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if ((eventType == XmlPullParser.START_TAG)
						&& (xml.getName().equals("nortices"))) {
					// Check if the version matches the release tabName.
					// When version is 0 every release tabName is parsed.
					parseNorticesTag(changelogBuilder, xml);
					// At lease one release tabName has been parsed.
					norticeFound = true;
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
		if (norticeFound) {
			return changelogBuilder.toString();
		}
		return "";

	}

	private void parseNorticesTag(StringBuilder licenseBuilder,
			XmlPullParser resParser) throws XmlPullParserException, IOException {
		licenseBuilder.append("<h1>Nortices for files: ").append("</h1>");

		licenseBuilder.append("<ul>");
		// Parse child nodes
		int eventType = resParser.getEventType();
		String license = null;
		while ((eventType != XmlPullParser.END_TAG)
				|| (resParser.getName().equals("file"))) {
			if ((eventType == XmlPullParser.START_TAG)
					&& (resParser.getName().equals("file"))) {
				eventType = resParser.next();
				licenseBuilder.append("<li>" + resParser.getText() + "</li>");
			} else if ((eventType == XmlPullParser.START_TAG)
					&& (resParser.getName().equals("license"))) {
				eventType = resParser.next();
				license = resParser.getText();
			}
			eventType = resParser.next();
		}
		licenseBuilder.append("</ul>");

		// Add license if available
		if (!TextUtils.isEmpty(license)) {
			licenseBuilder.append("<span class='license'>").append(license)
					.append("</span>");

		}
	}

	private String getStyle() {
		return String.format("<style type=\"text/css\">%s</style>", style);
	}
}
