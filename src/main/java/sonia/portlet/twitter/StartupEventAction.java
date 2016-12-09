
package sonia.portlet.twitter;

//~--- non-JDK imports --------------------------------------------------------

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;


//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static sonia.portlet.twitter.TwitterSiteAttributes.*;

/**
 *
 * @author th
 */

public class StartupEventAction extends SimpleAction
{

  /** Field description */
  private static final Log LOGGER =
    LogFactoryUtil.getLog(StartupEventAction.class);

  /** Class name for site attributes */
  private static final String CLASS_NAME = Group.class.getName();

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param companyId
   * @param className
   * @param fieldName
   * @param columnType
   * @param defaultData
   * @param typeSettings
   *
   * @throws PortalException
   * @throws SystemException
   */
  public static void addField(long companyId, String className,
    String fieldName, int columnType, Object defaultData,
    String typeSettings) throws SystemException, PortalException
  {
    ExpandoTable expandoTable;

    LOGGER.debug( "Startup check custom site attribute : " + fieldName );
    
    expandoTable = ExpandoTableLocalServiceUtil.getTable(companyId, className,
      ExpandoTableConstants.DEFAULT_TABLE_NAME);
    
    if ((expandoTable == null) || (expandoTable.getTableId() == 0))
    {
      try
      {
        expandoTable = ExpandoTableLocalServiceUtil.addTable(companyId,
          className, ExpandoTableConstants.DEFAULT_TABLE_NAME);
      }
      catch (PortalException | SystemException e)
      {
        LOGGER.error("Can't add Expando table.", e);
        return;
      }
    }

    long expandoTableId = expandoTable.getTableId();

    ExpandoColumn expandoColumn;

    expandoColumn = ExpandoColumnLocalServiceUtil.getColumn(expandoTableId,
      fieldName);

    if (expandoColumn == null)
    {
      LOGGER.info("Adding custom field [" + fieldName + "] to [" + className
        + "]");
      expandoColumn = ExpandoColumnLocalServiceUtil.addColumn(expandoTableId,
        fieldName, columnType, defaultData);
    }

    if (typeSettings != null)
    {
      LOGGER.info("Updating custom field settings for [" + fieldName + "] of ["
        + className + "]");
      ExpandoColumnLocalServiceUtil.updateTypeSettings(
      expandoColumn.getColumnId(), typeSettings);
    }
  }

  @Override
  public void run(String[] strings) throws ActionException
  {
    LOGGER.info("TWITTER: *** Startup Action ***");

    try
    {
      List<Company> companies = CompanyLocalServiceUtil.getCompanies();

      for (Company company : companies)
      {
        long companyId = company.getCompanyId();

        //J--
        // site attributes for twitter api access
        addField(companyId, CLASS_NAME, TWITTER_OAUTH_ACCESS_TOKEN, ExpandoColumnConstants.STRING, StringPool.BLANK, "height=0\nhidden=0\nindex-type=0\nsecret=1\nvisible-with-update-permission=0\nwidth=0\n");
        addField(companyId, CLASS_NAME, TWITTER_OAUTH_ACCESS_TOKEN_SECRET, ExpandoColumnConstants.STRING, StringPool.BLANK, "height=0\nhidden=0\nindex-type=0\nsecret=1\nvisible-with-update-permission=0\nwidth=0\n");
        addField(companyId, CLASS_NAME, TWITTER_OAUTH_CONSUMER_KEY, ExpandoColumnConstants.STRING, StringPool.BLANK, "height=0\nhidden=0\nindex-type=0\nsecret=1\nvisible-with-update-permission=0\nwidth=0\n");
        addField(companyId, CLASS_NAME, TWITTER_OAUTH_CONSUMER_SECRET, ExpandoColumnConstants.STRING, StringPool.BLANK, "height=0\nhidden=0\nindex-type=0\nsecret=1\nvisible-with-update-permission=0\nwidth=0\n");
        //J++
      }
    }
    catch (SystemException ex)
    {
      LOGGER.error(ex);
    }
    catch (PortalException ex)
    {
      Logger.getLogger(StartupEventAction.class.getName()).log(Level.SEVERE,
        null, ex);
    }
  }
}
