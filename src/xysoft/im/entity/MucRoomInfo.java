package xysoft.im.entity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;

/**
 * Smack里的RoomInfo并没有管理员和拥有者信息，MucRoomInfo加入以进行成员的持久化
 *
 */
public class MucRoomInfo {

	private  EntityBareJid room;
    private  String description;
    private  String name;
    private  String subject;
    private  int occupantsCount;
    private  boolean membersOnly;
    private  boolean moderated;
    private  boolean nonanonymous;
    private  boolean passwordProtected;
    private  boolean persistent;
    private  int maxhistoryfetch;
    private  List<String> contactJid;
    private  List<String> adminJid;
	private  List<String> ownerJid;
    private  String lang;
    private  String ldapgroup;
    private  Boolean subjectmod;
    private  URL logs;
    private  String pubsub;
    private  Form form;

    public MucRoomInfo(DiscoverInfo info) {
        final Jid from = info.getFrom();
        if (from != null) {
            this.room = info.getFrom().asEntityBareJidIfPossible();
        } else {
            this.room = null;
        }

        this.membersOnly = info.containsFeature("muc_membersonly");
        this.moderated = info.containsFeature("muc_moderated");
        this.nonanonymous = info.containsFeature("muc_nonanonymous");
        this.passwordProtected = info.containsFeature("muc_passwordprotected");
        this.persistent = info.containsFeature("muc_persistent");

        List<DiscoverInfo.Identity> identities = info.getIdentities();

        if (!identities.isEmpty()) {
            this.name = identities.get(0).getName();
        } else {

            this.name = "";
        }
        String subject = "";
        int occupantsCount = -1;
        String description = "";
        int maxhistoryfetch = -1;
        List<String> contactJid = null;
        List<String> adminJid = null;
        List<String> ownerJid = null;
        String lang = null;
        String ldapgroup = null;
        Boolean subjectmod = null;
        URL logs = null;
        String pubsub = null;
        // Get the information based on the discovered extended information
        form = Form.getFormFrom(info);
        if (form != null) {
            FormField descField = form.getField("muc#roominfo_description");
            if (descField != null && !descField.getValues().isEmpty()) {
                // Prefer the extended result description
                description = descField.getValues().get(0);
            }

            FormField subjField = form.getField("muc#roominfo_subject");
            if (subjField != null && !subjField.getValues().isEmpty()) {
                subject = subjField.getValues().get(0);
            }

            FormField occCountField = form.getField("muc#roominfo_occupants");
            if (occCountField != null && !occCountField.getValues().isEmpty()) {
                occupantsCount = Integer.parseInt(occCountField.getValues().get(
                                0));
            }

            FormField maxhistoryfetchField = form.getField("muc#maxhistoryfetch");
            if (maxhistoryfetchField != null && !maxhistoryfetchField.getValues().isEmpty()) {
                maxhistoryfetch = Integer.parseInt(maxhistoryfetchField.getValues().get(
                                0));
            }

            FormField contactJidField = form.getField("muc#roominfo_contactjid");
            if (contactJidField != null && !contactJidField.getValues().isEmpty()) {
                contactJid = contactJidField.getValues();
            }

            FormField adminJidField = form.getField("muc#roomconfig_roomadmins");
            if (adminJidField != null && !adminJidField.getValues().isEmpty()) {
            	adminJid = adminJidField.getValues();
            }

            FormField ownerJidField = form.getField("muc#roomconfig_roomowners");
            if (ownerJidField != null && !ownerJidField.getValues().isEmpty()) {
            	ownerJid = ownerJidField.getValues();
            }

            FormField langField = form.getField("muc#roominfo_lang");
            if (langField != null && !langField.getValues().isEmpty()) {
                lang = langField.getValues().get(0);
            }

            FormField ldapgroupField = form.getField("muc#roominfo_ldapgroup");
            if (ldapgroupField != null && !ldapgroupField.getValues().isEmpty()) {
                ldapgroup = ldapgroupField.getValues().get(0);
            }

            FormField subjectmodField = form.getField("muc#roominfo_subjectmod");
            if (subjectmodField != null && !subjectmodField.getValues().isEmpty()) {
                subjectmod = Boolean.valueOf(subjectmodField.getValues().get(0));
            }

            FormField urlField = form.getField("muc#roominfo_logs");
            if (urlField != null && !urlField.getValues().isEmpty()) {
                String urlString = urlField.getValues().get(0);
                try {
                    logs = new URL(urlString);
                } catch (MalformedURLException e) {

                }
            }

            FormField pubsubField = form.getField("muc#roominfo_pubsub");
            if (pubsubField != null && !pubsubField.getValues().isEmpty()) {
                pubsub = pubsubField.getValues().get(0);
            }
        }
        this.description = description;
        this.subject = subject;
        this.occupantsCount = occupantsCount;
        this.maxhistoryfetch = maxhistoryfetch;
        this.contactJid = contactJid;
        this.adminJid = adminJid;
        this.ownerJid = ownerJid;
        this.lang = lang;
        this.ldapgroup = ldapgroup;
        this.subjectmod = subjectmod;
        this.logs = logs;
        this.pubsub = pubsub;
    }


    public EntityBareJid getRoom() {
        return room;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSubject() {
        return subject;
    }

    public int getOccupantsCount() {
        return occupantsCount;
    }

    public boolean isMembersOnly() {
        return membersOnly;
    }

    public boolean isModerated() {
        return moderated;
    }

    public boolean isNonanonymous() {
        return nonanonymous;
    }

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public int getMaxHistoryFetch() {
        return maxhistoryfetch;
    }

    public List<String> getContactJids() {
        return contactJid;
    }
    
    public List<String> getAdminJid() {
		return adminJid;
	}


	public List<String> getOwnerJid() {
		return ownerJid;
	}

    public String getLang() {
        return lang;
    }

    public String getLdapGroup() {
        return ldapgroup;
    }

    public Boolean isSubjectModifiable() {
        return subjectmod;
    }

    public String getPubSub() {
        return pubsub;
    }

    public URL getLogsUrl() {
        return logs;
    }

    public Form getForm() {
        return form;
    }

}
