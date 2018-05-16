聊天对象类型：
群聊：c
单聊：d

消息体定义

<message to="wangxin@win7-1803071731/pc" from="admin@win7-1803071731/WIN7-1803071731" id="aac8a" type="chat">
  <body>msg here</body>
  <request xmlns="urn:xmpp:receipts"/>
  <nick xmlns="http://jabber.org/protocol/nick">admin</nick>
</message>

修改头像的iq
保存Vcard信息
processPacket-IQ:<iq id='kbecP-46' type='set'><vCard xmlns='vcard-temp'><FN>王昕</FN><PHOTO><BINVAL>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</BINVAL><TYPE>image/jpeg</TYPE></PHOTO><TEL><HOME/><VOICE/><NUMBER>13710637136</NUMBER></TEL></vCard></iq>

processPacket-IQ 服务器返回:
processPacket-IQ:<iq to='wangxin@win7-1803071731/pc' id='kbecP-46' type='result'></iq>
<iq to='wangxin@win7-1803071731/pc' id='kbecP-38' type='result'><vCard xmlns='vcard-temp'><FN>王昕</FN><PHOTO><BINVAL>/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCADIAMgDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDi/B3/ACOejf8AX5H/AOhCvqHA9BXzD4SQx+NtHUkcXkY4/wB6vo64l1NbmZYoojCEBjOCSTz7+uKchI0MD0H5UYHoKitWle2jM4Alx8wAxg/mampDEwPQUYHoKWigBMD0FGB6ClooATA9BRgegpaKAEwPQUYHoKWigBMD0FGB6ClooATA9BRgegpaKAEwPQUYHoKWigBMD0FGB6ClooATA9BRgegpaKAEwPQUYHoKWigBMD0FFLRQB8u+Dv8Akc9G/wCvyP8A9CFfQF/4usdPvprWS2uXeEgMyKpHTPrXz/4NIPjLRsf8/kf/AKEK9Y12G9j8SX0kdnNLG7L/AMsyVYDafx6YrvwVCnWm4z7ehwY6vUowTp9+1zsNJ8R2msLOYIZ18nbuDKO+emCfStWOVZGKhGGO7LiuU8DxTi81GSa2e3WTZgMhA/i9q7gsHE6+VtCcBiPvcZyPzx+FY4unCnWcYbG2DqTqUVKe5XwPSjA9KKK5jpDA9KMD0oooAMD0owPSiigAwPSjA9KKKADA9KMD0oooAMD0owPSiigAwPSjA9KKKADA9KMD0oooAMD0owPSiigAwPSjA9KKKADA9KKKKAPlvwWMeMtGB/5/I/8A0IV9SV8u+Dv+Rz0b/r8j/wDQhX1FTYkFOMjkYLHH1ptFIYUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFRSRu7grKUx2Ap0SGNcFy/uaAH0UUUAFFFIzqgyzBR6k4o2AWio/tEP/AD1T/voUVPPHuLmXc+d/CdvbL4s0kptZvtKE/LjHzCvoyvnPwf8A8jbpn/Xyn/oQr6MrSQIKKKKkYUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAVR1b/jzH++KvU140kXa6hh6EZrOtD2kHFdSZx5otHDrNqu0FraHljxu6L2ortPslv8A88I/++RRXmf2dPujk+rPyPnPwWCvjDSx5wkzOnQ9PmX/ABr6PMkanBdQfQmvmnwH/wAjlpv/AF3T/wBCFeka+IH8T6is5C5ZQrEn5fugn34JP4V9HhcN9Ym43tZDxeJ+rxUrXuz09XV/usG+hzTq4fwNbxPc6nCrF4x5eD0z970rtXSxsJMSXAjdlLYY5JAIBP5kfnWWJo+xqOF7muGre2pqdrXH0VEb/TFba+oQI3913Cn8jTorzT55hDFfRPIW2gKwOTgnA98K35GsDcfRR59j/wA/sPJx98dahGo6U3C6naseeFlUk4znofY0ATUUpltFgWZ7pEiboznaP1psVxYzsFivYnYgEKrAnnpxQAtFIbmxVnDXka7G2MWOAG9MnvSmayBAN5FkttHzDlvT60AFFMF3p+QDexKT0DsFz9M0q3enMUC6hbkvnaBIvOOuOaAHUVGb3TVkVDfwhmGRlhgj61Ofs4jST7QpRyArA5BPtQAyio0vdOkj8xL2Nl/2Tn9Kkt5rK7kkjtr2GaSI4dY3DFfqB0oAjkWUsDG4A7giliWRVIkcMc9QKfRQAUUUUAFFFFABRRRQB81eCYGh8ZaXuZTmdPunP8S/4177eeF9JvrqS5uLdmlkOWIkYZ/Wvn7wH/yOWm/9d0/9CFfTNaRqTpu8HYznThUVpq5Q0vRbHR2kNlEYzJjcS5PTp1+tN1XzJ76RZNIkuY44tqt5pCuCVJBGO2Bj6GtIda53W5LKDVrp7jWL6BhFvMcQYhV4XIx7kfrUznKb5pO7KhCMFyxVkWrTT7W+uZDLojQMIwyvIzAE46fhuNVbRZILxpo9AlhdbrJPmthzhlL4x0wx/OrkMselhdRn1WeW1uUURCWMsBnBHT+LGe1Za6hYm8uZF1u6dTIwERjchSwOO/bIPGKko6JtF09VWRrQGfJYbGb72M8Ht0FYcenowAj8OSR4Dni4Iw3oPzpslmkbRMNZuxmdsZQqMr1XlhkcH160wy28cKXkut3IEYZ+Ymyw5JIG7tz+lAGozyHRFtRo0oUBdsDkkA7xgZHbHPp2qta3NxZAS2/h+ZWKBT87HjAzwR/9fjvVa1+z3rpHF4jvdmwLyjKeueWJxnAPamutujS2o1y8SRFw8hjbb0UcHOMn+ZP0oAZJJLNLI8vhqVg0wbOXGT/e6e/6VGt3eJdRF/Cs8RFyVDK74AyMucA/X8K1rfw3cu5uF1q7CyMW2kMPp/FnHeopFgWSJv7evsC6bhNxDNgZQ9eORQA+QJcW6mbQCZUVtifP2UADO3jIAH4UtpaWz6Sb19EYXkMZCW7BsgdAAT7D/OarWV3YQwNB/bt9cSJvO6RXyMgDnp0P86r3N3YSQZk1yWNWt5G3GFt4G7OeG6jHAxQBaaArbhU8PyRbYkwokYqPVePQZqb7dOtmludDuGjG3Kh3wMZOeR61iPNYtpjuPEl+YozGWkjifIBwQMZ71p2WrWVhLPeNrF5dIwVfKkhchOp4HrhWoAlXMzyxNoEqIkxiGZXAZOm8YHTge+K0vDto8QurmbTfsM00nzL55k3gdD7dTxWVHPa2lwbj+2LorcT79rREqvAYKPQdPwJra8POj2LtHfS3il+JJEK4+Ucc/n+NAE560UHrRQAUUUUAFFFFABRRRQB81+Crd7fxnpgdlJM6Dg/7Qr6Ur5n8Cuz+MtM3MzYnTGTn+IV9MU2JCjrWPqlrrE2rSmDTdNmtCgVZJowz5yM5yenHT2rXopDMe5/4SUWgS1tbcP8ALhG2BFGR069P6fSr1lZSfaJ1vdOtdjEMjoinJyevGfT9atUUAZdtDrUCDGnWCZldmCEAhcHHbGeT370WlvqhnRbywtHiP328tMjPXHPue1alFAGRdWutxao7aZZWEVsYwUdkUMH75x261KtvqRLvPp9rJI6oCyxISxyNxOWGRjP+eulRQBnTnXWlJS1t8RvmJtq5C8ju3Bxj86he21V5Y1/sy0aPIDGRUOBnnHtjP/1616KAMq2s9VkhP2uw01Z9pG5IgVx8uOpz/e7joKqva+IEmkjg0zT1g8zYvypjy8+lb9FAGU9rqfmFf7K08xhyARGpymRjqe38xUkFlfyaXci6srIXaO32dfKUqy4+XPp1IrRooAyrGw1EyQC7sdPRGXMjRwLlW9Bz6cVvW0EUHmLDbJAu7+FQA/A54/Ln0qtRQAHrRRRQAUUUUAFFFFABRRRQB80eC43tfGeliYbS1wgHPU7hX0vXzB4RleTxnou9i2LyPr/vCvp+nISCiiikMKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigD5g8JRSReNNFEiFSbyPqP9oV9P18w+E55Z/Gei+Y2SLyM9B/eFfTUkqxAb2xnpgE02JElFMMi+WJNwKnGCBmkjmjmBMcitjrjtSGSUVGk8cjsiSKzL94DtyR/MGpOfWgAoo59aOfWgAoo59aOfWgAoo59aOfWgAoo59aOfWgAoo59aOfWgAoo59aOfWgCKSVo2AEbMD3A6UsUjSKSyFDnGDUnPrRz60AFFHPrRz60AFFHPrRz60AFFHPrRQB8v+D0ZfGei7lK/wCmR9R/tCvpa9sY76IRyPIoB6xuVP5gg9q+bvC13Lc+M9F8zH/H7GeB/tCvpuqkJEfkjyRECcDHPfOc5oiiEZJznIA6Afy+tSUVIyrb2EVrcTTo0heY5fc2QT6/0q1RRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAfLvg7/kc9G/6/I//AEIV9RV8x+E5zN4x0QFEXF5GcqOT8wr6cqpCQUUUVIwooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAPl3wd/yOejf9fkf/oQr6ir578MeGNbTxJp14dJdILW7RpnVlO0AgnjPpzXux1W3XqJRj/Yq5RdyVKNr3L1FUI9XtpmcRiVimN3yHihtYtU+95g/4DU8r7Bzx3uX6Kz4tatJmdY/MYpjd8h4zQ2s2ifeLj/gNPlfYOePc0KKzotbs5ndIzIzIAWAQ8Zzj+Rok1uzhx5hdcnHK0cr7ApRezNGisyLXrGeR442kZ0AZgEPAOcfyNQnxTpAk8v7WhfONoIJocWugKcWrpmzRWXFr9hPK0UTSM6ruICHgU99atI/v+YPqho5JbWDnjvc0aKzItfsJ5TFG0jOF3ECM9Krt4t0RW2m+i3ZxjcM5/Opl7vxaFwTn8Gpt0VlReIdPnmMMTu0gXdgIenSnSa9YwnEjupPQFDVckuxHPHuadFZMPiPTricwRSSPKF3FRGenH+IpjeKtHVSxvEwDgnI4Pp+hocZLdDUk9mbNFZUHiHT7qcQQSPJIVLhVQ/dGOf1FWzfooyYp8evlmk4taNApJ7MtUVi3firSbF1W6neNmGRmM81HH4z0KVsJe5P+4RVqlN6pMh1YJ2bRvUVjy+J9LhTe8zhfURsR+gqofHfh4HBvj/37b/ChUaj2iwdWmvtI6Oiub/4Tzw7/wA/3/kNv8KKfsKv8r+4PbU/5l95ztheywJceX1MhPPrgVZur6W5hR3C+arqoCdcdSf0xRRXpTgnY8WE2rozr+8uU0zU0tEL3DxHYucZbbxXm+j2/jF/EdjPqVrNHbJLucrN8oHPbcc9aKKidKMpJs0p15Rg4o9Cur64jsdRW2XdM8RCLnGTtOK8406LxjNr9nNf2ssduk4Z9sxwFznpu5ooqqlGMpJsiliJwi4o9Mg1GSB7gpneyL+m6udju7m5ureWSCf7SW3TPMhA/A9PpxRRU1qalUgvM7MFVlChVa6L9H+BsR3zwzTbM5eJR+rVgeJPPGpQRW3mBD5eViztPTOduR+ooop4impOLObCVZRjI3oL/wCy3DOrEsUA/U1uWWoaTc2U/wBunljm/hHJ9+O2evX2oorSthadSUakt47HNCq4vuc9FqMkEzsrMWZNtZwnsv8AhHXtRbgTsBnCMH3D3xgjJzyen0FFFebm9KMnB+v6H1HDVaUY1Ev7r/Mu2+ovbT+YoO4x7f1FLd3TyJE5uPNfqFAOV+v5H86KK9uKSPmHN6kMGpTW92ZVPzFCP5VzrySuiuLqVYAoDIN24d2XZnB5zz34zRRXLisPCrbm7nfgK0qfNbyOq8MXM8d4jDcXFqRjv1SvRDrJa8kbzY/K8raMyD09M9aKKwxcIuSLws2os8y8UeYJrfzeuzv9TXPE4PFFFerh9aaPLrr94y7Z6vd2R+SQsndW5BrTVdN1xcKFtrv0/hY0UVU4q3MiYN3szDv9Pn0+YxzoVPY9jRRRVQd4psJKzsf/2Q==</BINVAL><TYPE>image/jpeg</TYPE></PHOTO><TEL><HOME/><VOICE/><NUMBER>13710637136</NUMBER></TEL></vCard></iq>


图片、文件的交互

别人发给我：
<iq to="wangxin@win7-1803071731/pc" from="test1@win7-1803071731/Spark" id="qGEbZ-202" type="set">
  <si xmlns="http://jabber.org/protocol/si" id="jsi_1084472191504457540" mime-type="image/png" profile="http://jabber.org/protocol/si/profile/file-transfer">
    <file xmlns="http://jabber.org/protocol/si/profile/file-transfer" name="image_IW.png" size="1665" date="2018-05-06T09:19:54.476+00:00">
      <desc>Sending file</desc>
    </file>
    <feature xmlns="http://jabber.org/protocol/feature-neg">
      <x xmlns="jabber:x:data" type="form">
        <field var="stream-method" type="list-single">
          <option>
            <value>http://jabber.org/protocol/bytestreams</value>
          </option>
          <option>
            <value>http://jabber.org/protocol/ibb</value>
          </option>
        </field>
      </x>
    </feature>
  </si>
</iq>

让我回复用哪种方式接收，bytestreams 还是ibb
如果两个协议都不支持，则我返回：
<iq to="test1@win7-1803071731/Spark" id="qGEbZ-202" type="error">
  <error type="cancel">
    <feature-not-implemented xmlns="urn:ietf:params:xml:ns:xmpp-stanzas"/>
  </error>
</iq>


######群聊######

被邀请（smack方式）
<message to="wangxin@win7-1803071731" from="mc1@muc.win7-1803071731" id="w1h78-205">
  <x xmlns="http://jabber.org/protocol/muc#user">
    <invite from="test1@win7-1803071731/Spark">
      <reason>请加入会议。</reason>
    </invite>
  </x>
</message>

针对离线用户的邀请：
<message to="test2@win7-1803071731" type="chat">
  <body>请加入会议</body>
  <x xmlns="xytalk:muc:invitation">
    <roomid>1526287366130@muc.win7-1803071731</roomid>
    <roomName>群组名称</roomName>
  </x>
</message>



被邀请（其他邀请方式）不支持此方式
<message to="wangxin@win7-1803071731" from="test1@win7-1803071731/1120190887-tigase-7">
  <x xmlns="jabber:x:conference" jid="mc1@muc.win7-1803071731"/>
</message>

查询会议室信息

<iq to="wangxin@win7-1803071731/pc" from="mc2@muc.win7-1803071731" id="xPOnJ-37" type="result">
  <query xmlns="http://jabber.org/protocol/disco#info">
    <identity category="conference" name="会议室" type="text"/>
    <feature var="http://jabber.org/protocol/muc"/>
    <feature var="muc_semianonymous"/>
    <feature var="muc_moderated"/>
    <feature var="muc_membersonly"/>
    <feature var="muc_persistent"/>
    <feature var="muc_public"/>
    <feature var="muc_unsecured"/>
    <x xmlns="jabber:x:data" type="result">
      <field var="FORM_TYPE" type="hidden">
        <value>http://jabber.org/protocol/muc#roominfo</value>
      </field>
      <field label="Room creation date" var="muc#roominfo_creationdate">
        <value>2018-05-08T07:23:55Z</value>
      </field>
      <field label="Number of occupants" var="muc#roominfo_occupants">
        <value>2</value>
      </field>
      <field label="Current discussion topic" var="muc#roominfo_subject">
        <value/>
      </field>
      <field label="Whether occupants allowed to invite others" var="muc#roomconfig_allowinvites">
        <value>1</value>
      </field>
      <field label="Whether occupants may change the subject" var="muc#roomconfig_changesubject">
        <value>0</value>
      </field>
      <field label="Whether logging is enabled" var="muc#roomconfig_enablelogging">
        <value>0</value>
      </field>
      <field label="Natural language room name" var="muc#roomconfig_lang">
        <value>会议室</value>
      </field>
      <field label="Maximum number of room occupants" var="muc#roomconfig_maxusers">
        <value/>
      </field>
      <field label="Whether room is members-only" var="muc#roomconfig_membersonly">
        <value>1</value>
      </field>
      <field label="Whether room is moderated" var="muc#roomconfig_moderatedroom">
        <value>1</value>
      </field>
      <field label="Whether a password is required to enter" var="muc#roomconfig_passwordprotectedroom">
        <value>0</value>
      </field>
      <field label="Whether room is persistent" var="muc#roomconfig_persistentroom">
        <value>1</value>
      </field>
      <field label="Roles for which presence is broadcast" var="muc#roomconfig_presencebroadcast">
        <value>moderator</value>
        <value>participant</value>
        <value>visitor</value>
      </field>
      <field label="Whether room is publicly searchable" var="muc#roomconfig_publicroom">
        <value>1</value>
      </field>
      <field label="Full list of room admins" var="muc#roomconfig_roomadmins">
        <value>test2@win7-1803071731</value>
      </field>
      <field label="Short description of room" var="muc#roomconfig_roomdesc">
        <value>会议</value>
      </field>
      <field label="Natural language room name" var="muc#roomconfig_roomname">
        <value>会议室</value>
      </field>
      <field label="Full list of room owners" var="muc#roomconfig_roomowners">
        <value>test1@win7-1803071731</value>
        <value>wangxin@win7-1803071731</value>
      </field>
    </x>
  </query>
</iq>

