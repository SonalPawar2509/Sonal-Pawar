# Monefy App Testing Prioritization and Charters

## Table of Contents
- [Reasons for the Assigned Priority to the Respective Charters](#reasons-for-the-assigned-priority-to-the-respective-charters)
- [Charter 1: Initial Setup and Configuration (High Priority)](#charter-1-initial-setup-and-configuration-high-priority)
- [Charter 2: Core Financial(Income/Expense) Recording Features (High Priority)](#charter-2-core-financialincomeexpense-recording-features-high-priority)
- [Charter 3: Explore All Expense Icons and Home Page UI (High Priority)](#charter-3-explore-all-expense-icons-and-home-page-ui-high-priority)
- [Charter 4: Left Menu - Currency Support and Reports](#charter-4-left-menu---currency-support-and-reports)
- [Charter 5: Right Menu Settings and Data Management](#charter-5-right-menu-settings-and-data-management)
- [Charter 6: Multi-device Synchronization (Low Priority)](#charter-6-multi-device-synchronization-low-priority)
- [Charter 7: UI Orientation Support (Low Priority)](#charter-7-ui-orientation-support-low-priority)
- [Charter 8: Application Performance and Resource Usage (Medium Priority)](#charter-8-application-performance-and-resource-usage-medium-priority)
- [Charter 9: Security and Privacy Features (High Priority)](#charter-9-security-and-privacy-features-high-priority)
- [Observations and Bugs](#observations-and-bugs)
- [Identified Bug](#identified-bug)
- [Risk Assessment for Money Management Applications](#risk-assessment-for-money-management-applications)

---

## Reasons for the Assigned Priority to the Respective Charters:

**Charter 1:**  
This has been assigned high priority as it covers the basic smoke/sanity checks related to app setup and onboarding. Ensuring that the app is properly installed and ready for use is fundamental to the overall user experience.

**Charter 2:**  
Also marked as high priority, since it focuses on income and expense management, which is the core functionality of the Monefy app. Accuracy in data entry and calculations is critical, making this area essential to test thoroughly.

**Charter 3:**  
This charter also holds high priority. The visual representation of financial data on the home screen is key for user understanding and trust. These visuals must function flawlessly to ensure clarity and usability.

**Charter 4:**  
No specific priority is assigned at this point. This charter needs to be broken down into high-level test cases before determining its overall priority. It currently includes a mix of medium and low priority areas:
- Yearly and monthly reports are considered low priority, especially in the free version, due to limited user engagement.
- Daily and weekly reports, being more commonly accessed, fall into the medium priority category.
- Currency validation, however, is high priority, as the app must function correctly across different countries and currencies.

**Charter 5:**  
This is categorized as medium priority in the free version. During initial use, users typically interact more with home screen features before exploring settings, account creation, or categories. However, general settings hold significance from a conversion standpoint (free to paid users), justifying a medium level of testing priority.

**Charters 6 & 7:**  
These are considered low priority in the free version, as they do not contribute to core functionality or revenue generation. Thus, from a testing perspective, they can be deprioritized.

**Charters 8 & 9:**  
These pertain to performance and security testing, respectively. While not assigned fixed priorities here, their significance may vary depending on the involvement of dedicated teams and the depth of performance and security evaluations being planned.

---

## Charter 1: Initial Setup and Configuration (High Priority)

**Mission:** Explore the initial setup process and basic configuration (Free version - if supported Account creation part)  
**Areas:** Installation, initial settings, Onboarding Screens, and Landing on Home Page  
**Priority:** P1

**Testing Notes:**
- Verify smooth installation process
- Initial onboarding screen navigation and clicking on the buttons to move further
- Free version, provided enforced advertising screen (its functionality to go for paid Version - this is all happens before we land on the HOME PAGE)
- Explore configuration for e.g., allowing notifications of the app

---

## Charter 2: Core Financial(Income/Expense) Recording Features (High Priority)

**Mission:** Explore the income and expense recording functionality.  
**Areas:** Income/Expense entry, categorization, date selection, notes  
**Priority:** P1

**Testing Notes:**
- Add various expense entries with different categories
- Add income entries with different categories
- Test date selection for past and future transactions
- Verify calculation accuracy for entered amounts
- Test notes field for income/expense both
- Verify editing and deletion of recorded transactions

---

## Charter 3: Explore All Expense Icons and Home Page UI (High Priority)

**Mission:** To check if all the graphical expense icons present on the UI of the home page are clickable as well as functionality wise working as expected.  
**Areas:** Home page all UI icons, balance circle, back and forth navigation on HOME SCREEN  
**Priority:** P1

**Testing Notes:**
- Verify accuracy of expense icons shown on the HOME PAGE
- Verify balance circle
- Verify navigation back and forth to the home page, while testing expense icons
- Verify expense/income functionality overall by navigating through the icons.

---

## Charter 4: Left Menu - Currency Support and Reports

**Mission:** Explore budget setting and monitoring features.  
**Areas:** Budget creation, alerts, tracking against budgets

**Testing Notes:**
- Set up category budgets with various amounts
- Test budget period options (weekly, monthly)
- Verify budget tracking and percentage calculations
- Test budget alerts and notifications (if available)
- Explore budget vs. actual spending reports

---

## Charter 5: Right Menu Settings and Data Management

**Mission & Areas:** Explore categories creation for income/expense, Account creation for more money sources, explore limited features of free Version, explore general settings

**Testing Notes:**
- Set up categories income and expense
- Set up more money accounts
- Check limited features of free app version
- Explore general settings

---

### Charter 5: Create, Restore, Clear Data Management Functionality

**Mission:** Explore data backup, restore, and export capabilities.  
**Areas:** Create, restore, clear data functionality  
**Priority:** Low

**Testing Notes:**
- Test backup options and supported formats of file in free Version

---

## Charter 6: Multi-device Synchronization (Low Priority)

**Mission:** Explore data synchronization across multiple devices.

**Findings:**  
We do not have this feature in the free version as the account creation doesn't happen in the free version. However alternatively back up functionality can be used.

---

## Charter 7: UI Orientation Support (Low Priority)

**Mission:** Explore portrait and landscape modes

**Findings:**  
App is locked in portrait mode so landscape mode is not supported.

---

## Charter 8: Application Performance and Resource Usage (Medium Priority)

---

## Charter 9: Security and Privacy Features (High Priority)

**Mission:** Explore security mechanisms and privacy protections  
**Areas:** Authentication, data protection, privacy controls

**Testing Notes:**
- Test passcode/PIN protection features
- Verify privacy of financial data
- Test timeout and automatic lockout features
- Review permissions requested by the application

---

## Observations and Bugs

### General Observations:
- The Monefy app lacks any visible authentication or data protection policy notice for users.
- There is a buried link within the settings tab that leads to the Terms of Use, but considering the app collects user data, it does not clearly communicate any privacy assurance or data protection practices.
- The app does not support adding a PIN or any additional security feature through mobile settings, which reduces overall data security for users.

### Functionality Observations:
- The app allows deletion of any category, including default ones like "Salary". There are no system-protected categories, and deletion is permitted without confirmation pop-ups, increasing the risk of accidental data loss.

---

## Identified Bug

**Charter:** Onboarding  
**Priority:** Low  
**Severity:** Low

**Bug Summary:**  
On Android devices, users are unable to navigate back through the onboarding screens once they proceed.

**Steps to Reproduce:**
1. Install and open the Monefy app.
2. Tap on the “GET STARTED” button to move to the second onboarding screen.
3. Attempt to go back to the first screen to re-read the initial information.
4. Observe that, despite the presence of pagination dots, backward navigation is not allowed.

**Actual Result:**  
The user is forced to move forward through the onboarding process, with no option to revisit previous screens, even though the UI suggests otherwise.

**Expected Result:**  
Users should be able to navigate freely between onboarding screens if there is useful information on each. Alternatively, the app should reduce the number of screens or update the UI to reflect the one-way navigation.

---

## Risk Assessment for Money Management Applications

### Security and Privacy Risks
**Concerns:** Potential unauthorized access and data exposure.  
**Mitigations:** Implement robust encryption, secure authentication methods, session timeouts, and limit app permissions.

### Usability Risks
**Concerns:** Inconsistent user experience and potential accessibility issues.  
**Mitigations:** Prioritize thorough UX testing, accessibility support, and incorporate in-app help features.

### Regulatory Compliance Risks
**Concerns:** Risks of non-compliance with regulations like GDPR and CCPA.  
**Mitigations:** Conduct regular legal audits, ensure region-specific compliance, and maintain transparent privacy policies.

### Technical Risks
**Concerns:** Challenges such as compatibility issues, high battery consumption, and performance degradation.  
**Mitigations:** Execute extensive device testing, optimize app performance, and consistently monitor resource usage.

### Business Continuity Risks
**Concerns:** Dependencies on third-party services, potential server downtimes, and update failures.  
**Mitigations:** Develop redundancy protocols, provide offline support where possible, and rigorously test all app updates before rollout.
