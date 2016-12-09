package sonia.portlet.twitter;

//~--- non-JDK imports --------------------------------------------------------

import com.google.common.base.Strings;

import com.liferay.faces.util.portal.WebKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.expando.model.ExpandoBridge;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import sonia.annotation.portlet.SoniaPortletPreference;
import sonia.annotation.portlet.SoniaPortletPreferencesHandler;
import static sonia.portlet.twitter.TwitterSiteAttributes.*;

/**
 *
 * @author th
 */
@ManagedBean(name = "preferences")
@RequestScoped
public class PreferencesBean implements Serializable
{

  /**
   * Field description
   */
  private static final Log logger =
    LogFactoryUtil.getLog(PreferencesBean.class);

  /** Field description */
  private static final long serialVersionUID = 4581239562028888412L;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @PostConstruct
  public void initialize()
  {
    logger.debug("PreferencesBean:initialize");

    response = null;
    screenName = null;

    PortletRequest request =
      (PortletRequest) FacesContext.getCurrentInstance().getExternalContext()
        .getRequest();
    PortletPreferences preferences = request.getPreferences();

    ThemeDisplay themeDisplay =
      (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

    Group siteGroup = themeDisplay.getSiteGroup();

    ExpandoBridge siteAttributes = siteGroup.getExpandoBridge();

    //
    twitterOauthConsumerKey =
      (String) siteAttributes.getAttribute(TWITTER_OAUTH_CONSUMER_KEY);
    twitterOauthConsumerSecret =
      (String) siteAttributes.getAttribute(TWITTER_OAUTH_CONSUMER_SECRET);
    twitterOauthAccessToken =
      (String) siteAttributes.getAttribute(TWITTER_OAUTH_ACCESS_TOKEN);
    twitterOauthAccessTokenSecret =
      (String) siteAttributes.getAttribute(TWITTER_OAUTH_ACCESS_TOKEN_SECRET);

    SoniaPortletPreferencesHandler.load(preferences, this);

    //
    logger.trace("twitter oauth consumer key=" + twitterOauthConsumerKey);
    logger.trace("twitter oauth consumer secret=" + twitterOauthConsumerSecret);
    logger.trace("twitter oauth access token=" + twitterOauthAccessToken);
    logger.trace("twitter oauth access token secret="
      + twitterOauthAccessTokenSecret);

    initialized = false;

    if (Strings.isNullOrEmpty(twitterOauthConsumerKey)
      || Strings.isNullOrEmpty(twitterOauthConsumerSecret)
      || Strings.isNullOrEmpty(twitterOauthAccessToken)
      || Strings.isNullOrEmpty(twitterOauthAccessTokenSecret))
    {
      logger.debug("Twitter OAuth vars not available");
    }
    else
    {
      twitter = TwitterConfig.getTwitter(twitterOauthConsumerKey,
        twitterOauthConsumerSecret, twitterOauthAccessToken,
        twitterOauthAccessTokenSecret);

      try
      {
        if (twitterUserId > 0)
        {
          user = twitter.showUser(twitterUserId);
          initialized = true;
        }
      }
      catch (TwitterException ex)
      {
        logger.error("get twitter user for id=" + twitterUserId, ex);
      }
    }
  }

  /**
   * Method description
   *
   */
  public void save()
  {

    logger.debug("save");

    PortletRequest request =
      (PortletRequest) FacesContext.getCurrentInstance().getExternalContext()
        .getRequest();
    PortletPreferences preferences = request.getPreferences();

    //
    logger.debug("twitter user id=" + twitterUserId);

    SoniaPortletPreferencesHandler.store(preferences, this);

    initialize();
  }

  /**
   * Method description
   *
   */
  public void search()
  {
    logger.debug("search for: " + screenName);

    if (screenName != null)
    {
      try
      {
        response = twitter.searchUsers(screenName, 0);

        for (User u : response)
        {
          logger.debug(u.getId() + " / " + u.getScreenName() + " / "
            + u.getName());
        }

      }
      catch (TwitterException ex)
      {
        logger.error("Search twitter screenname", ex);
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Get the value of response
   *
   * @return the value of response
   */
  public ResponseList<User> getResponse()
  {
    return response;
  }

  /**
   * Get the value of screenName
   *
   * @return the value of screenName
   */
  public String getScreenName()
  {
    return screenName;
  }

  /**
   * Get the value of twitter
   *
   * @return the value of twitter
   */
  public Twitter getTwitter()
  {
    return twitter;
  }

  /**
   * Get the value of twitterOauthAccessToken
   *
   * @return the value of twitterOauthAccessToken
   */
  public String getTwitterOauthAccessToken()
  {
    return twitterOauthAccessToken;
  }

  /**
   * Get the value of twitterOauthAccessTokenSecret
   *
   * @return the value of twitterOauthAccessTokenSecret
   */
  public String getTwitterOauthAccessTokenSecret()
  {
    return twitterOauthAccessTokenSecret;
  }

  /**
   * Get the value of twitterOauthConsumerKey
   *
   * @return the value of twitterOauthConsumerKey
   */
  public String getTwitterOauthConsumerKey()
  {
    return twitterOauthConsumerKey;
  }

  /**
   * Get the value of twitterOauthConsumerSecret
   *
   * @return the value of twitterOauthConsumerSecret
   */
  public String getTwitterOauthConsumerSecret()
  {
    return twitterOauthConsumerSecret;
  }

  /**
   * Get the value of twitterUserId
   *
   * @return the value of twitterUserId
   */
  public long getTwitterUserId()
  {
    return twitterUserId;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public User getUser()
  {
    return user;
  }

  /**
   * Get the value of initialized
   *
   * @return the value of initialized
   */
  public boolean isInitialized()
  {
    if (!initialized)
    {
      initialize();
    }

    // logger.debug("is initialized = " + initialized);
    return initialized;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isResponseAvailable()
  {
    return response != null;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Set the value of screenName
   *
   * @param screenName new value of screenName
   */
  public void setScreenName(String screenName)
  {
    this.screenName = screenName;
  }

  /**
   * Set the value of twitterUserId
   *
   * @param twitterUserId new value of twitterUserId
   */
  public void setTwitterUserId(long twitterUserId)
  {
    this.twitterUserId = twitterUserId;
  }

  //~--- fields ---------------------------------------------------------------

  /**
   * Field description
   */
  private boolean initialized;

  /**
   * Field description
   */
  private ResponseList<User> response;

  /**
   * Field description
   */
  private String screenName;

  /**
   * Field description
   */
  private Twitter twitter;

  /**
   * Field description
   */
  private String twitterOauthAccessToken;

  /**
   * Field description
   */
  private String twitterOauthAccessTokenSecret;

  /**
   * Field description
   */
  private String twitterOauthConsumerKey;

  /**
   * Field description
   */
  private String twitterOauthConsumerSecret;

  /**
   * Field description
   */
  @SoniaPortletPreference
  private long twitterUserId;

  /**
   * Field description
   */
  private twitter4j.User user;
}
