package sonia.portlet.twitter;

//~--- non-JDK imports --------------------------------------------------------

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author th
 */

@ManagedBean(name = "twitter")
@RequestScoped
public class TwitterTimelineBean implements Serializable
{

  /** Field description */
  private static final Log logger =
    LogFactoryUtil.getLog(TwitterTimelineBean.class);

  /** Field description */
  private static final long serialVersionUID = -6268996891483435582L;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  public void loadTimeline()
  {
    logger.debug("loadTimeline");

    timeline = TwitterConfig.getTimeline(preferences.getTwitterUserId());
    loadingComplete = true;

    logger.debug("loadTimeline ... done");
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<FormatedStatus> getTimeline()
  {
    return timeline;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public FormatedUser getUser()
  {
    return new FormatedUser(preferences.getUser());
  }

  /**
   * Get the value of loadingComplete
   *
   * @return the value of loadingComplete
   */
  public boolean isLoadingComplete()
  {
    return loadingComplete;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param preferences
   */
  public void setPreferences(PreferencesBean preferences)
  {
    this.preferences = preferences;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @PostConstruct
  void initialize()
  {
    logger.debug("TwitterTimelineBean:initialize");
    loadingComplete = false;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean loadingComplete;

  /** Field description */
  @ManagedProperty(value = "#{preferences}")
  private PreferencesBean preferences;

  /** Field description */
  private List<FormatedStatus> timeline;
}
