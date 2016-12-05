package sonia.portlet.twitter;

//~--- non-JDK imports --------------------------------------------------------

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import twitter4j.auth.AccessToken;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author th
 */

public class TwitterConfig implements Serializable
{

  /** Field description */
  private static final long serialVersionUID = -8765903172257544671L;

  /** Field description */
  private final static TwitterConfig singleton = new TwitterConfig();

  /** Field description */
  private static final Log logger = LogFactoryUtil.getLog(TwitterConfig.class);

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  private TwitterConfig()
  {
    twitter = TwitterFactory.getSingleton();
    initialized = false;

    timelineCache = CacheBuilder.newBuilder().expireAfterWrite(16,
      TimeUnit.MINUTES).build(new CacheLoader<Long, List<FormatedStatus>>()
    {

      @Override
      public List<FormatedStatus> load(Long key) throws Exception
      {
        logger.debug("load timeline into cache = " + key);

        List<FormatedStatus> timeline = new ArrayList<>();

        Paging paging = new Paging(1, 8);
        List<Status> sl = twitter.getUserTimeline(key, paging);

        for (Status s : sl)
        {
          timeline.add(new FormatedStatus(s));
        }

        return timeline;
      }
    });

  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param twitterUserId
   *
   * @return
   */
  public static List<FormatedStatus> getTimeline(long twitterUserId)
  {
    return singleton._getTimeline(twitterUserId);
  }

  /**
   * Method description
   *
   *
   *
   * @param twitterOauthConsumerKey
   * @param twitterOauthConsumerSecret
   * @param twitterOauthAccessToken
   * @param twitterOauthAccessTokenSecret
   *
   * @return
   */
  public static Twitter getTwitter(String twitterOauthConsumerKey,
    String twitterOauthConsumerSecret, String twitterOauthAccessToken,
    String twitterOauthAccessTokenSecret)
  {
    return singleton._getTwitter(twitterOauthConsumerKey,
      twitterOauthConsumerSecret, twitterOauthAccessToken,
      twitterOauthAccessTokenSecret);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param twitterUserId
   *
   * @return
   */
  private List<FormatedStatus> _getTimeline(long twitterUserId)
  {
    logger.debug("_getTimeline = " + twitterUserId);

    List<FormatedStatus> t = null;

    try
    {
      t = timelineCache.get(twitterUserId);
    }
    catch (ExecutionException ex)
    {
      logger.error("Can not get timeline.", ex);
    }

    return t;
  }

  /**
   * Method description
   *
   *
   *
   * @param twitterOauthConsumerKey
   * @param twitterOauthConsumerSecret
   * @param twitterOauthAccessToken
   * @param twitterOauthAccessTokenSecret
   *
   * @return
   */
  private synchronized Twitter _getTwitter(String twitterOauthConsumerKey,
    String twitterOauthConsumerSecret, String twitterOauthAccessToken,
    String twitterOauthAccessTokenSecret)
  {
    if (!initialized)
    {
      logger.debug("initialize twitter auth");
      twitter.setOAuthConsumer(twitterOauthConsumerKey,
        twitterOauthConsumerSecret);

      AccessToken accessToken = new AccessToken(twitterOauthAccessToken,
                                  twitterOauthAccessTokenSecret);

      twitter.setOAuthAccessToken(accessToken);
      initialized = true;
    }

    return twitter;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final LoadingCache<Long, List<FormatedStatus>> timelineCache;

  /** Field description */
  private final Twitter twitter;

  /** Field description */
  private boolean initialized;
}
