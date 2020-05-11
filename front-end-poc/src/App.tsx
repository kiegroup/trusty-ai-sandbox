import React, { useEffect, useState } from "react";
import "./App.scss";
import {
  Avatar,
  Brand,
  Card,
  CardBody,
  Gallery,
  GalleryItem,
  Nav,
  NavList,
  Page,
  PageHeader,
  PageSection,
  PageSectionVariants,
  PageSidebar,
  TextContent,
  Text,
  Title,
  NavItem,
} from "@patternfly/react-core";
import { Switch, Route, NavLink, Redirect, useLocation } from "react-router-dom";
import AuditOverview from "./Audit/AuditOverview/AuditOverview";
import AuditDetail from "./Audit/AuditDetail";
import ScrollToTop from "./Shared/ScrollToTop";
import AppBreadcrumbs from "./Shared/components/AppBreadcrumbs";
const imgBrand =
  "data:image/svg+xml;base64,PHN2ZyBlbmFibGUtYmFja2dyb3VuZD0ibmV3IDAgMCAyMjUgMzUiIHZpZXdCb3g9IjAgMCAyMjUgMzUiIHdpZHRoPSIyMjUiIGhlaWdodD0iMzUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgeG1sbnM6eGxpbms9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkveGxpbmsiPjxsaW5lYXJHcmFkaWVudCBpZD0iYSIgZ3JhZGllbnRVbml0cz0idXNlclNwYWNlT25Vc2UiIHgxPSIxNy40Nzk5IiB4Mj0iMTcuNDc5OSIgeTE9IjIuMDM2OSIgeTI9IjM0LjExMTMiPjxzdG9wIG9mZnNldD0iMCIgc3RvcC1jb2xvcj0iIzAwZjFmZiIvPjxzdG9wIG9mZnNldD0iMSIgc3RvcC1jb2xvcj0iIzAwNzZjMSIvPjwvbGluZWFyR3JhZGllbnQ+PHBhdGggZD0ibTEyLjMgMjYuNy01LjIgMS4yLTctOS45IDE3LTE2LjYuNC0uNC40LjQgMTcgMTYuNi03IDkuOS01LjItMS4yLTUuMiA3LjN6bTUuMiA1LjYgNC4yLTUuOC0xLjMtLjMtMi44IDQuMS0zLTQuMS0xLjMuM3ptLjEtMy44IDItMy0yLjEtMjAuMy0yLjEgMjAuM3ptOS44LTEuOCA2LjEtOC42LTEwLjMtMTAuMSA1LjUgNi43LTMuMSA0LjMgMSAyLjMtMy4yIDQuNXptLTE5LjkgMCA0LjEtLjktMy4yLTQuNSAxLTIuMy0zLjEtNC4zIDUuNC02LjctMTAuMyAxMC4xem01LjItMS4yIDEuMy0uMy0zLjktNS4zLS41IDEuMnptOS42IDAgMy4xLTQuNC0uNS0xLjItMy45IDUuM3ptLTEuOC0xLjQgMy45LTUuMy01LjgtMTMuMnptLTYuMSAwIDItMTguNS01LjggMTMuM3ptLTQuNS02LjIgNS44LTEzLjEtOC4xIDkuOXptMTUuMiAwIDIuMy0zLjItOC4xLTkuOXoiIGZpbGw9InVybCgjYSkiLz48ZyBmaWxsPSIjZmZmIj48cGF0aCBkPSJtNTAuOCAyNHYtMTIuMWg1LjJjLjggMCAxLjQuMSAxLjkuM3MuOS41IDEuMy45Yy4zLjQuNi44LjcgMS4yLjEuNS4yLjkuMiAxLjQgMCAuMyAwIC42LS4xLjlzLS4yLjYtLjMuOS0uMy41LS41LjhjLS4yLjItLjUuNS0uOC43cy0uNy4zLTEuMS40LS44LjItMS4zLjJoLTIuOXY0LjR6bTUuMy02LjdjLjMgMCAuNSAwIC44LS4xLjItLjEuNC0uMi41LS40cy4yLS4zLjMtLjUuMS0uNC4xLS42IDAtLjQtLjEtLjVjMC0uMi0uMS0uNC0uMy0uNS0uMS0uMi0uMy0uMy0uNS0uNHMtLjUtLjItLjgtLjJoLTN2My4yeiIvPjxwYXRoIGQ9Im03Ni4yIDI0LS45LTIuNWgtNC43bC0uOSAyLjVoLTIuNWw0LjYtMTIuMWgyLjNsNC42IDEyLjF6bS0yLjktNy44Yy0uMS0uMS0uMS0uMy0uMi0uNXMtLjEtLjQtLjItLjZjMCAuMi0uMS40LS4yLjZzLS4xLjQtLjIuNWwtMS4yIDMuMWgzLjJ6Ii8+PHBhdGggZD0ibTkxLjkgMTQuMnY5LjhoLTIuM3YtOS44aC0zLjV2LTIuM2g5LjN2Mi4zeiIvPjxwYXRoIGQ9Im0xMTAgMTQuMnY5LjhoLTIuM3YtOS44aC0zLjV2LTIuM2g5LjN2Mi4zeiIvPjxwYXRoIGQ9Im0xMjMuMSAyNHYtMTIuMWg4LjN2Mi4yaC01Ljl2Mi41aDMuNXYyLjJoLTMuNXYyLjloNi4zdjIuM3oiLz48cGF0aCBkPSJtMTQxLjUgMjR2LTEyLjFoNS42Yy44IDAgMS40LjEgMS45LjNzLjkuNSAxLjIuOC41LjguNyAxLjJjLjEuNS4yLjkuMiAxLjQgMCAuMy0uMS43LS4xIDEtLjEuNC0uMi43LS40IDFzLS40LjYtLjcuOS0uNi41LTEgLjZsMi4zIDQuOGgtMi42bC0yLjMtNC41aC0yLjR2NC42em01LjctNi43Yy4zIDAgLjUgMCAuOC0uMS4yLS4xLjQtLjIuNS0uNC4xLS4xLjItLjMuMy0uNXMuMS0uNC4xLS42IDAtLjQtLjEtLjZjMC0uMi0uMS0uNC0uMi0uNXMtLjMtLjMtLjUtLjQtLjUtLjEtLjgtLjFoLTMuM3YzLjJ6Ii8+PHBhdGggZD0ibTE2OC44IDI0LTQuOC03LjFjLS4xLS4xLS4yLS4zLS4zLS40LS4xLS4yLS4yLS4zLS4yLS41di41LjUgN2gtMi4zdi0xMi4xaDIuMmw0LjcgN2MuMS4xLjIuMy4zLjQuMS4yLjIuMy4zLjUgMC0uMiAwLS4zIDAtLjVzMC0uMyAwLS40di03aDIuM3YxMi4xeiIvPjxwYXRoIGQ9Im0xODEuMyAyNHYtMTIuMWg4LjF2Mi4yaC01Ljh2Mi41aDMuN3YyLjJoLTMuN3Y1LjJ6Ii8+PHBhdGggZD0ibTE5OC44IDI0di0xMi4xaDIuM3Y5LjloNS44djIuMnoiLz48cGF0aCBkPSJtMjE4LjIgMjR2LTQuN2wtNC41LTcuNGgyLjZsMyA0LjkgMy00LjloMi43bC00LjUgNy40djQuN3oiLz48L2c+PC9zdmc+Cg==";
const imgAvatar =
  "data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHN2ZyBlbmFibGUtYmFja2dyb3VuZD0ibmV3IDAgMCAzNiAzNiIgdmVyc2lvbj0iMS4xIiB2aWV3Qm94PSIwIDAgMzYgMzYiIHhtbDpzcGFjZT0icHJlc2VydmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxzdHlsZSB0eXBlPSJ0ZXh0L2NzcyI+CgkvKnN0eWxlbGludC1kaXNhYmxlKi8KCS5zdDB7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDojRkZGRkZGO30KCS5zdDF7ZmlsdGVyOnVybCgjYik7fQoJLnN0MnttYXNrOnVybCgjYSk7fQoJLnN0M3tmaWxsLXJ1bGU6ZXZlbm9kZDtjbGlwLXJ1bGU6ZXZlbm9kZDtmaWxsOiNCQkJCQkI7fQoJLnN0NHtvcGFjaXR5OjAuMTtmaWxsLXJ1bGU6ZXZlbm9kZDtjbGlwLXJ1bGU6ZXZlbm9kZDtlbmFibGUtYmFja2dyb3VuZDpuZXcgICAgO30KCS5zdDV7b3BhY2l0eTo4LjAwMDAwMGUtMDI7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDojMjMxRjIwO2VuYWJsZS1iYWNrZ3JvdW5kOm5ldyAgICA7fQoJLypzdHlsZWxpbnQtZW5hYmxlKi8KPC9zdHlsZT4KCQkJPGNpcmNsZSBjbGFzcz0ic3QwIiBjeD0iMTgiIGN5PSIxOC41IiByPSIxOCIvPgoJCTxkZWZzPgoJCQk8ZmlsdGVyIGlkPSJiIiB4PSI1LjIiIHk9IjcuMiIgd2lkdGg9IjI1LjYiIGhlaWdodD0iNTMuNiIgZmlsdGVyVW5pdHM9InVzZXJTcGFjZU9uVXNlIj4KCQkJCTxmZUNvbG9yTWF0cml4IHZhbHVlcz0iMSAwIDAgMCAwICAwIDEgMCAwIDAgIDAgMCAxIDAgMCAgMCAwIDAgMSAwIi8+CgkJCTwvZmlsdGVyPgoJCTwvZGVmcz4KCQk8bWFzayBpZD0iYSIgeD0iNS4yIiB5PSI3LjIiIHdpZHRoPSIyNS42IiBoZWlnaHQ9IjUzLjYiIG1hc2tVbml0cz0idXNlclNwYWNlT25Vc2UiPgoJCQk8ZyBjbGFzcz0ic3QxIj4KCQkJCTxjaXJjbGUgY2xhc3M9InN0MCIgY3g9IjE4IiBjeT0iMTguNSIgcj0iMTgiLz4KCQkJPC9nPgoJCTwvbWFzaz4KCQk8ZyBjbGFzcz0ic3QyIj4KCQkJPGcgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoNS4wNCA2Ljg4KSI+CgkJCQk8cGF0aCBjbGFzcz0ic3QzIiBkPSJtMjIuNiAxOC4xYy0xLjEtMS40LTIuMy0yLjItMy41LTIuNnMtMS44LTAuNi02LjMtMC42LTYuMSAwLjctNi4xIDAuNyAwIDAgMCAwYy0xLjIgMC40LTIuNCAxLjItMy40IDIuNi0yLjMgMi44LTMuMiAxMi4zLTMuMiAxNC44IDAgMy4yIDAuNCAxMi4zIDAuNiAxNS40IDAgMC0wLjQgNS41IDQgNS41bC0wLjMtNi4zLTAuNC0zLjUgMC4yLTAuOWMwLjkgMC40IDMuNiAxLjIgOC42IDEuMiA1LjMgMCA4LTAuOSA4LjgtMS4zbDAuMiAxLTAuMiAzLjYtMC4zIDYuM2MzIDAuMSAzLjctMyAzLjgtNC40czAuNi0xMi42IDAuNi0xNi41YzAuMS0yLjYtMC44LTEyLjEtMy4xLTE1eiIvPgoJCQkJPHBhdGggY2xhc3M9InN0NCIgZD0ibTIyLjUgMjZjLTAuMS0yLjEtMS41LTIuOC00LjgtMi44bDIuMiA5LjZzMS44LTEuNyAzLTEuOGMwIDAtMC40LTQuNi0wLjQtNXoiLz4KCQkJCTxwYXRoIGNsYXNzPSJzdDMiIGQ9Im0xMi43IDEzLjJjLTMuNSAwLTYuNC0yLjktNi40LTYuNHMyLjktNi40IDYuNC02LjQgNi40IDIuOSA2LjQgNi40LTIuOCA2LjQtNi40IDYuNHoiLz4KCQkJCTxwYXRoIGNsYXNzPSJzdDUiIGQ9Im05LjQgNi44YzAtMyAyLjEtNS41IDQuOS02LjMtMC41LTAuMS0xLTAuMi0xLjYtMC4yLTMuNSAwLTYuNCAyLjktNi40IDYuNHMyLjkgNi40IDYuNCA2LjRjMC42IDAgMS4xLTAuMSAxLjYtMC4yLTIuOC0wLjYtNC45LTMuMS00LjktNi4xeiIvPgoJCQkJPHBhdGggY2xhc3M9InN0NCIgZD0ibTguMyAyMi40Yy0yIDAuNC0yLjkgMS40LTMuMSAzLjVsLTAuNiAxOC42czEuNyAwLjcgMy42IDAuOWwwLjEtMjN6Ii8+CgkJCTwvZz4KCQk8L2c+Cjwvc3ZnPgo=";

const App = () => {
  const [isMobileView, setIsMobileView] = useState(false);
  const [isNavOpenDesktop, setIsNavOpenDesktop] = useState(true);
  const [isNavOpenMobile, setIsNavOpenMobile] = useState(false);
  let location = useLocation();

  const onNavToggleDesktop = () => {
    setIsNavOpenDesktop(!isNavOpenDesktop);
  };

  const onNavToggleMobile = () => {
    setIsNavOpenMobile(!isNavOpenMobile);
  };

  const handlePageResize = (stuff: { windowSize: number; mobileView: boolean }) => {
    // closing sidebar menu when resolution is < 1200
    if (stuff.windowSize < 1200) {
      if (!isMobileView) setIsMobileView(true);
    } else {
      if (isMobileView) setIsMobileView(false);
    }
  };

  const PageNav = (
    <Nav aria-label="Nav" theme="dark">
      <NavList>
        <NavItem isActive={location.pathname === "/dashboard"}>
          <NavLink to="/dashboard">Domain Monitoring</NavLink>
        </NavItem>
        <NavItem isActive={location.pathname === "/op-dashboard"}>
          <NavLink to="/op-dashboard">Operational Monitoring</NavLink>
        </NavItem>
        <NavItem isActive={location.pathname.startsWith("/audit")}>
          <NavLink to="/audit">Audit Investigation</NavLink>
        </NavItem>
      </NavList>
    </Nav>
  );

  const Header = (
    <PageHeader
      logo={<Brand src={imgBrand} alt="Patternfly Logo" />}
      avatar={<Avatar src={imgAvatar} alt="Avatar image" />}
      showNavToggle
      onNavToggle={isMobileView ? onNavToggleMobile : onNavToggleDesktop}
      isNavOpen={isMobileView ? isNavOpenMobile : isNavOpenDesktop}
    />
  );
  const Sidebar = (
    <PageSidebar nav={PageNav} isNavOpen={isMobileView ? isNavOpenMobile : isNavOpenDesktop} theme="dark" />
  );
  const pageId = "main-content-page-layout-manual-nav";

  useEffect(() => {
    document.title = "Trusty PoC";
  }, []);

  return (
    <>
      <ScrollToTop />
      <Page
        header={Header}
        sidebar={Sidebar}
        mainContainerId={pageId}
        breadcrumb={AppBreadcrumbs}
        onPageResize={handlePageResize}>
        <Switch>
          <Route exact path="/">
            <Redirect to="/dashboard" />
          </Route>
          <Route path="/dashboard">
            <PageSection variant={PageSectionVariants.light}>
              <TextContent>
                <Title size="4xl" headingLevel="h1">
                  Domain Monitoring Dashboard
                </Title>
                <Text component="p">Here will be the monitoring dashboard</Text>
              </TextContent>
            </PageSection>
            <PageSection style={{ height: "50em" }} isFilled={true}>
              <Gallery gutter="md">
                {Array.apply(0, Array(20)).map((x, i) => (
                  <GalleryItem key={i}>
                    <Card>
                      <CardBody>This is a card</CardBody>
                    </Card>
                  </GalleryItem>
                ))}
              </Gallery>
            </PageSection>
          </Route>
          <Route path="/op-dashboard">
            <PageSection variant={PageSectionVariants.light}>
              <TextContent>
                <Title size="4xl" headingLevel="h1">
                  Operational Monitoring Dashboard
                </Title>
                <Text component="p">Here will be the monitoring dashboard</Text>
              </TextContent>
            </PageSection>
            <PageSection style={{ height: "50em" }} isFilled={true}>
              <Gallery gutter="md">
                {Array.apply(0, Array(20)).map((x, i) => (
                  <GalleryItem key={i}>
                    <Card>
                      <CardBody>This is a card</CardBody>
                    </Card>
                  </GalleryItem>
                ))}
              </Gallery>
            </PageSection>
          </Route>
          <Route exact path="/audit">
            <AuditOverview />
          </Route>
          <Route path="/audit/:executionType/:executionId">
            <AuditDetail />
          </Route>
        </Switch>
      </Page>
    </>
  );
};

export default App;
