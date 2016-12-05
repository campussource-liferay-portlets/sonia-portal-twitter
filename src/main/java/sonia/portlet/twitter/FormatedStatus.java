package sonia.portlet.twitter;

//~--- non-JDK imports --------------------------------------------------------

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;

//~--- JDK imports ------------------------------------------------------------

import java.text.SimpleDateFormat;

import java.util.Date;

/**
 *
 * @author th
 */

public class FormatedStatus
{

  /** Field description */
  private static final Log logger = LogFactoryUtil.getLog(FormatedStatus.class);

  /** Field description */
  private static final SimpleDateFormat dateFormat =
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param status
   */
  public FormatedStatus(Status status)
  {
    this.status = status;

    String t = status.getText();

    if (status.isRetweet())
    {
      t = status.getRetweetedStatus().getText();
    }

    /*
     * int i = t.indexOf("https://t.co/");
     *
     * if (i < 0)
     * {
     * i = t.indexOf("http://t.co/");
     * }
     *
     * if (i >= 0)
     * {
     * summary = t.substring(0, i);
     * }
     * else
     * {
     * summary = t;
     * }
     */

    StringBuilder s = new StringBuilder();
    int i = 0;
    int o = 0;

    while (i < t.length())
    {
      int c = t.charAt(i);

      if (c == '@')
      {
        s.append(t.substring(o, i));
        o = i;

        while (i < t.length())
        {
          c = t.charAt(i);

          if ((c == ' ') || (c == '\t'))
          {
            String n = t.substring(o + 1, i);

            s.append("<a target='_blank' href='https://twitter.com/");
            s.append(n);
            s.append("'>@");
            s.append(n);
            s.append("</a>");
            o = i;

            break;
          }

          i++;
        }
      }
      else if (c == '#')
      {
        s.append(t.substring(o, i));
        o = i;

        while (i < t.length())
        {
          c = t.charAt(i);

          if ((c == ' ') || (c == '\t'))
          {
            break;
          }

          i++;
        }

        String n = t.substring(o + 1, i);

        s.append("<a target='_blank' href='https://twitter.com/hashtag/");
        s.append(n);
        s.append("?src=hash'>#");
        s.append(n);
        s.append("</a>");
        o = i;
      }
      else if ( t.substring(i).startsWith("https://t.co/") || t.substring(i).startsWith("http://t.co/") )
      {
        s.append(t.substring(o, i));
        o = i;

        while (i < t.length())
        {
          c = t.charAt(i);

          if ((c == ' ') || (c == '\t'))
          {
            break;
          }

          i++;
        }
        
        //
        s.append("<a target='blank' href='");
        s.append(t.substring(o, i));
        s.append("'>");
        s.append(t.substring(o, i));
        s.append("</a>");
        o = i;
      }

      i++;
    }

    s.append(t.substring(o, t.length()));
    // s.append("<hr/>");
    // s.append(t);
    summary = s.toString();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getCreatedAt()
  {
    return status.getCreatedAt();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDate()
  {
    return dateFormat.format(status.getCreatedAt());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDisplayURL()
  {
    return status.getURLEntities()[0].getDisplayURL();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getExpandedURL()
  {
    return status.getURLEntities()[0].getExpandedURL();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getRetweetUserScreenName()
  {
    return status.getRetweetedStatus().getUser().getScreenName();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSummary()
  {

    /*
     * StringBuilder s = new StringBuilder(summary);
     *
     * int o = 0;
     *
     * while(( o = s.indexOf("@", o )) >= 0 )
     * {
     * int e = s.indexOf(" ", o);
     * String n = s.substring(o+1,e);
     * logger.debug("n=" + n);
     *
     * s.insert( e, "</a>" );
     * String hr = "<a target='_blank' href='https://twitter.com/" + n + "'>";
     * s.insert(o, hr );
     *
     * o+=hr.length()+1;
     * }
     *
     * if ( isUrlPresent() )
     * {
     * s.append("<a target='_blank' href='").append(getUrl()).append("'>");
     * s.append(getUrl()).append("</a>");
     * }
     *
     * if ( status.getHashtagEntities().length > 0 )
     * {
     * for( HashtagEntity h : status.getHashtagEntities())
     * {
     *   s.append(", <a target='_blank' href='https://twitter.com/hashtag/").append(h.getText());
     *   s.append("?src=hash'>#").append(h.getText()).append("</a>");
     * }
     * }
     *
     * return s.toString();
     */
    return summary;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getText()
  {
    return status.getText();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getUrl()
  {
    return status.getURLEntities()[0].getURL();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isRetweet()
  {
    return status.isRetweet();
  }

  /**
   * Get the value of urlPresent
   *
   * @return the value of urlPresent
   */
  public boolean isUrlPresent()
  {
    return status.getURLEntities().length > 0;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final Status status;

  /** Field description */
  private final String summary;

  /** Field description */
  private String url;
}
