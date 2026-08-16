# Cloud Build Quick Guide

## Build an APK

GitHub → repository → **Actions** → **Build BUPOLY Android App** → **Run workflow** → `apk` → **Run workflow**.

When the job finishes, download **BUPOLY-SRMS-APK** from the workflow's **Artifacts** section.

## Build an AAB

Use the same steps but select `aab`.

## Build both

Select `both` to receive both artifacts from one workflow run.
