# Maven Network / Proxy Troubleshooting

If `mvn validate` or `mvn test` fails with errors like:

- `status code: 403, reason phrase: Forbidden`
- `Could not transfer artifact ... from/to central`
- `CONNECT tunnel failed`

then the issue is usually repository access through proxy/mirror configuration (not Java code).

## Quick diagnosis

1. Check proxy env vars:
   - `env | rg -i 'proxy'`
2. Check active Maven settings:
   - `cat ~/.m2/settings.xml`
3. Check direct reachability through current path:
   - `curl -I https://repo.maven.apache.org/maven2/`

## Recommended fix (team/CI)

Use an internal artifact mirror (Nexus/Artifactory) and mirror all repositories through it.

1. Copy `.mvn/settings.xml.example` to one of:
   - `~/.m2/settings.xml` (developer machine), or
   - `.mvn/settings.xml` (project-local for CI/container usage).
2. Set your actual mirror URL and credentials.
3. Run:
   - `scripts/mvn-safe.sh -U validate`
   - `scripts/mvn-safe.sh -U test`

## Notes

- In locked-down environments, direct access to Maven Central may be blocked by policy.
- If direct egress is blocked and no internal mirror is configured, builds cannot resolve dependencies.
- If direct egress is allowed but proxy is misconfigured, disable proxy for Maven or fix proxy allow-list for Maven Central hosts.
