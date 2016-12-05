package sonia.portlet.twitter;

//~--- non-JDK imports --------------------------------------------------------

import twitter4j.User;

/**
 *
 * @author th
 */

public class FormatedUser
{

  /**
   * Constructs ...
   *
   *
   * @param user
   */
  public FormatedUser(User user)
  {
    this.user = user;

    String t = user.getDescription();

    int i = t.indexOf("https://t.co/");

    if (i < 0)
    {
      i = t.indexOf("http://t.co/");
    }

    if (i >= 0)
    {
      summary = t.substring(0, i);
      url = t.substring(i);
      urlPresent = true;
    }
    else
    {
      summary = t;
    }

  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return user.getDescription();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return user.getName();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getOriginalProfileImageURLHttps()
  {
    return user.getOriginalProfileImageURLHttps();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getScreenName()
  {
    return user.getScreenName();
  }

  /**
   * Get the value of summary
   *
   * @return the value of summary
   */
  public String getSummary()
  {
    return summary;
  }

  /**
   * Get the value of url
   *
   * @return the value of url
   */
  public String getUrl()
  {
    return url;
  }

  /**
   * Get the value of urlPresent
   *
   * @return the value of urlPresent
   */
  public boolean isUrlPresent()
  {
    return urlPresent;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final String summary;

  /** Field description */
  private String url;

  /** Field description */
  private boolean urlPresent;

  /** Field description */
  private final User user;
}
