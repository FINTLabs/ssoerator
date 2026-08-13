# SSOerator

## Helm charts

Deployment manifests are packaged as Helm charts:

- `charts/ssoerator` contains the operator deployment, service, service account, RBAC, and OnePassword item.
- `charts/ssoerator-crd` contains the generated `flaisssoes.fintlabs.no` CRD.
- `charts/ssoerator/values-alpha.yaml`, `values-beta.yaml`, and `values-api.yaml` replace the old kustomize overlays.

The GitHub Actions workflow stamps chart and app versions before publishing the charts to the Flais OCI Helm repository.

Adds the following resources:

- [x] Service
- [x] Deployment
- [x] IngressRoute
- [x] Middleware
- [x] NamOAuthClientApplicationResource
- [x] ConfigMap
