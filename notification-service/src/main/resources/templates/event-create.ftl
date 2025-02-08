<tr>
    <td valign="top" style="width:40px;background:#ffffff">&nbsp;</td>
    <td valign="top" style="width:560px;background:#ffffff">
        <table cellpadding="0" cellspacing="0" width="560" align="center" style="border-collapse:collapse">
            <tbody>
            <tr>
                <td style="height: 20px; background: #ffffff;">&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <span style="font-family:Arial,Helvetica,sans-serif;display:block;font-size:18pt;color:#004E87;font-weight:normal;margin:0">
                        Dear ${name},
                    </span>
                </td>
            </tr>
            <tr>
                <td style="height:20px;background:#ffffff">&nbsp;</td>
            </tr>
            <tr>
                <td style="background:#ffffff">
                    <span style="font-family:Arial,Helvetica,sans-serif;display:block;font-size:16pt;color:#000000;font-weight:normal;margin:0;line-height:1.45">
                        Greetings from Ticker Master!!
                    </span><br>
                </td>
            </tr>
            <tr>
                <td style="height:31px;background:#ffffff;text-align: justify">
                    <span style="font-family:Arial,Helvetica,sans-serif;display:block;font-size:15pt;color:#000000;font-weight:normal;margin:0;line-height:1.45">
                        You are Most Welcome to Ticker Master. We are happy to have you as our customer.
                    </span>
                </td>
            </tr>

            <tr>
                <td style="height:20px;background:#ffffff">&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <span style="font-family:Arial,Helvetica,sans-serif;display:block;font-size:16pt;color:#004E87;font-weight:bold;margin:0">
                        Event Details:
                    </span>
                </td>
            </tr>
            <tr>
                <td>
                    <span style="font-family:Arial,Helvetica,sans-serif;display:block;font-size:14pt;color:#000000;font-weight:normal;margin:0;line-height:1.45">
                        <strong>Event Name:</strong> ${eventName} <br>
                        <strong>Date:</strong> ${eventDate} <br>
                        <strong>Status:</strong> ${eventStatus} <br>
                        <strong>Venue:</strong> ${venueName} <br>
                        <strong>Address:</strong> ${venueAddress} <br>
                       <strong>Google Map Location:</strong> <a href="${venueLocation}">${venueLocation}</a><br>
                    </span>
                </td>
            </tr>
            <tr>
                <td style="height:20px;background:#ffffff">&nbsp;</td>
            </tr>
            <#if performers?has_content>
                <tr>
                    <td>
                    <span style="font-family:Arial,Helvetica,sans-serif;display:block;font-size:16pt;color:#004E87;font-weight:bold;margin:0">
                        Performers:
                    </span>
                    </td>
                </tr>
                <tr>
                    <td>
                        <ul style="font-family:Arial,Helvetica,sans-serif;font-size:14pt;color:#000000;font-weight:normal;margin:0;line-height:1.45">
                            <#list performers as performer>
                                <li>${performer.name}</li>
                            </#list>
                        </ul>
                    </td>
                </tr>
            </#if>

            </tbody>
        </table>
    </td>
    <td valign="top" style="width:40px;background:#ffffff">&nbsp;</td>
</tr>
<tr>
    <td valign="top" style="width:40px;height:25px;background:#ffffff;border-bottom:1px solid #ffffff" colspan="3">
        &nbsp;
    </td>
</tr>
